package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.dto.UserDto;
import com.interswitch.smartmoveserver.model.request.PassportUser;
import com.interswitch.smartmoveserver.repository.UserApprovalRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.util.FileParser;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.RandomUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.interswitch.smartmoveserver.helper.JwtHelper.isInterswitchEmail;

/**
 * @author adebola.owolabi
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PassportService passportService;

    @Autowired
    IswCoreService iswCoreService;

    @Autowired
    WalletService walletService;

    @Autowired
    CardService cardService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserSettingsService userSettingsService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private UserApprovalRepository userApprovalRepository;

    @Autowired
    private PageUtil pageUtil;

    @Value("${smartmove.url}")
    private String portletUri;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findOwners(List<Enum.Role> roles) {
        List<User> users = new ArrayList<>();
        for (Enum.Role role : roles) {
            users.addAll(userRepository.findAllByRole(role));
        }
        return users;
    }

    public PageView<User> findAllPaginated(int page, int size, String principal) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<User> pages = userRepository.findAll(pageable);
        return new PageView<>(pages.getTotalElements(), pages.getContent());
    }

    public void setUp(User user) {
        Optional<User> exists = userRepository.findByUsername(user.getUsername());
        if (exists.isPresent()) return;
        else {
            PassportUser passportUser = passportService.findUser(user.getUsername());
            if (passportUser == null) passportUser = passportService.createUser(user);
            user.setUsername(passportUser.getUsername());
            user.setPassword(passportUser.getPassword());
            //iswCoreService.createUser(user);
            userRepository.save(user);
        }
    }

    public void setUpS(User user) {
        Optional<User> exists = userRepository.findByUsername(user.getUsername());
        if (exists.isPresent()) return;
        else {
            userRepository.save(user);
        }
    }
    //if user role is admin, user.owner must be null
    //else must have value
    //if principal is admin user.owner must be populated
    //if not, bounce request

    public User save(User user, String principal) {
        boolean exists = userRepository.existsByUsername(user.getEmail());
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        User owner = user.getOwner();
        if (owner == null) {
            owner = findByUsername(principal);
        }
        PassportUser passportUser = passportService.findUser(user.getEmail());
        if (passportUser != null) {
            save(passportUser, user, owner);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists. Kindly ask user to login with their Quickteller credentials");
        }

        //TODO :: see below
        boolean makerChecker = checkForMakerChecker(user, owner, principal);
        if (makerChecker) {
            user.setEnabled(false);
            //save the user on smartmove db
            save(null, user, owner);
            sendMakerCheckerEmail(user, owner);
            UserApproval approval = new UserApproval();
            approval.setOwner(owner);
            approval.setUsr(user);
            userApprovalRepository.save(approval);
            return user;
        }

        //setting default password for all users
        String randomDefaultPassword = new RandomUtil(8).nextString();
        user.setPassword(randomDefaultPassword);
         passportUser = passportService.createUser(user);
        //iswCoreService.createUser(user);
        user.setEnabled(true);
        save(passportUser, user, owner);
        //setting the password at this point because it was set to empty string in the previous line.
        user.setPassword(randomDefaultPassword);
        sendUserSetUpEmail(user, owner);
        return user;
    }

    private User saveAsAdmin(PassportUser passportUser) {
        User user = passportService.buildUser(passportUser);
        user.setRole(Enum.Role.ISW_ADMIN);
        user.setEnabled(true);
        return save(passportUser, user, null);
    }

    private User save(PassportUser passportUser, User user, User owner) {
        if (passportUser == null)
            user.setUsername(user.getEmail());
        else {
            user.setUsername(passportUser.getUsername());
            user.setPassword(passportUser.getPassword());
        }
        Enum.Role role = user.getRole();
        user.setOwner(null);
        user.setTillStatus(Enum.TicketTillStatus.OPEN);
        if (!role.equals(Enum.Role.ISW_ADMIN)) {
            user.setOwner(owner);
        }
        if (user.getPicture()!=null && user.getPicture().getSize()>0) {
            Document doc = documentService.saveDocument(new Document(user.getPicture()));
            user.setPictureUrl(doc.getUrl());
        }

        //setting password as empty string here as we don't want to store passwords on smartmove
        user.setPassword("");
        userRepository.save(user);
        if (role.equals(Enum.Role.AGENT)) {
            walletService.autoCreateForUser(user);
            cardService.autoCreateForUser(user);
        }
        return user;
    }

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }

    public User findById(long id, String principal) {
        if (securityUtil.isOwner(principal, id))
            return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
    }

    public List<User> findAllByRole(String principal, Enum.Role role) {
        Optional<User> user = userRepository.findByUsername(principal);
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if (securityUtil.isOwnedEntity(role)) {
            if (securityUtil.isOwnedEntity(user.get().getRole())) {
                return userRepository.findAllByRoleAndOwner(role, user.get());
            } else {
                return userRepository.findAllByRole(role);
            }
        } else {
            if (securityUtil.isOwnedEntity(user.get().getRole()))
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
            return userRepository.findAllByRole(role);
        }
    }

    public PageView<User> findAllPaginatedByRole(String principal, long owner, Enum.Role role, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal);
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(role)) {
                if (securityUtil.isOwnedEntity(user.get().getRole())) {
                    Page<User> pages = userRepository.findAllByRoleAndOwner(pageable, role, user.get());
                    return new PageView<>(pages.getTotalElements(), pages.getContent());
                } else {
                    Page<User> pages = userRepository.findAllByRole(pageable, role);
                    return new PageView<>(pages.getTotalElements(), pages.getContent());
                }
            } else {
                if (securityUtil.isOwnedEntity(user.get().getRole()))
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
                Page<User> pages = userRepository.findAllByRole(pageable, role);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwnedEntity(role)) {
                if (securityUtil.isOwner(principal, owner)) {
                    Optional<User> ownerUser = userRepository.findById(owner);
                    if (!ownerUser.isPresent())
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found");
                    Page<User> pages = userRepository.findAllByRoleAndOwner(pageable, role, ownerUser.get());
                    return new PageView<>(pages.getTotalElements(), pages.getContent());
                }
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }

    public User findOrCreateByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        }
        PassportUser passportUser = passportService.findUser(username);
        if (passportUser != null) {
            if (isInterswitchEmail(passportUser.getEmail()))
                return saveAsAdmin(passportUser);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You do not have permission to this resource");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You do not have permission to this resource");
    }

    public User update(User user, String principal) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            if (user.getPicture().getSize()>0) {
                Document doc = documentService.saveDocument(new Document(user.getPicture()));
                user.setPictureUrl(doc.getUrl());
            }
            if(user.getOwner() == null) {
                User owner = findByUsername(principal);
                user.setOwner(owner);
            }
            return userRepository.save(user);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
    }

    public void delete(long id, String principal) {
        Optional<User> existing = userRepository.findById(id);
        if (existing.isPresent())
            userRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }
    }

    public Long countByRoleAndOwner(User user, Enum.Role role) {
        return userRepository.countByRoleAndOwner(role, user);
    }

    public Long countByRole(String principal, User owner, Enum.Role role) {
        Optional<User> user = userRepository.findByUsername(principal);
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if (owner == null) {
            if (securityUtil.isOwnedEntity(role)) {
                if (securityUtil.isOwnedEntity(user.get().getRole()))
                    return userRepository.countByRoleAndOwner(role, user.get());
                else
                    return userRepository.countByRole(role);
            } else {
                if (securityUtil.isOwnedEntity(user.get().getRole()))
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
                return userRepository.countByRole(role);
            }
        } else {
            if (securityUtil.isOwnedEntity(role)) {
                if (securityUtil.isOwner(principal, owner.getId())) {
                    return userRepository.countByRoleAndOwner(role, owner);
                }
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public boolean upload(MultipartFile file, String principal) throws IOException {
        User owner = findByUsername(principal);
        List<User> savedUsers = new ArrayList<>();
        if(file.getSize()>1){
            FileParser<UserDto> fileParser = new FileParser<>();
            List<UserDto> userDtoList = fileParser.parseFileToEntity(file, UserDto.class);
            userDtoList.forEach(userDto->{
                savedUsers.add(save(mapToUser(userDto, owner),principal));
            });
        }
        return savedUsers.size()>1;
    }

    private User mapToUser(UserDto userDto, User owner){

        return User.builder()
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .mobileNo(userDto.getMobileNo())
                .password(userDto.getPassword())
                .role(convertToRoleEnum(userDto.getRole()))
                .tillStatus(Enum.TicketTillStatus.OPEN)
                .enabled(isEnabled(userDto.getEnabled()))
                .owner(owner)
                .build();
    }

    private Enum.Role convertToRoleEnum(String role){
        //  ISW_ADMIN, REGULATOR, OPERATOR, EXECUTIVE, SERVICE_PROVIDER, INSPECTOR, TICKETER, VEHICLE_OWNER, AGENT, DRIVER, COMMUTER
        return Enum.Role.valueOf(role.toUpperCase());
    }

    private boolean isEnabled(String enabledStatus){
        return enabledStatus.equalsIgnoreCase("true") || enabledStatus.startsWith("true");
    }

    public List<UserApproval> getApprovals(String principal) {
        List<UserApproval> approvals = new ArrayList<>();
        Optional<User> owner = userRepository.findByUsername(principal);
        if (owner.isPresent()) {
            approvals = userApprovalRepository.findAllByOwner(owner.get());
        }
        return approvals;
    }

    public boolean approveUser(String principal, long id) {
        Optional<UserApproval> userApproval = userApprovalRepository.findById(id);
        if (userApproval.isPresent()) {
            UserApproval approval = userApproval.get();
            String owner = approval.getOwner() != null ? approval.getOwner().getUsername() : "";
            if (owner.equals(principal)) {
                approval.setApproved(true);
                if(userApprovalRepository.save(approval).isApproved()){
                    //setting default password for all users
                    User user = approval.getUsr();
                    user.setPassword(new RandomUtil(8).nextString());
                    log.info("Username and Password ===>"+user.getUsername()+" and "+user.getPassword());
                    PassportUser passportUser = passportService.createUser(user);
                    user.setEnabled(true);
                    if(passportUser!=null) userRepository.save(user);
                    sendUserSetUpEmail(user, approval.getOwner());
                }
                return true;
            }
        }
        return false;
    }

    private boolean checkForMakerChecker(User user, User owner, String principal) {
        Optional<User> creatorUser = userRepository.findByUsername(principal);
        if (creatorUser.isPresent())
            if (creatorUser.get().getRole().equals(Enum.Role.ISW_ADMIN)) {
                UserSettings userSettings = userSettingsService.findByOwner(owner.getUsername());
                //send maker checker email
                if (userSettings != null) return userSettings.isMakerCheckerEnabled();
            }
        return false;
    }

    private void sendMakerCheckerEmail(User user, User owner) {
        Map<String, Object> params = new HashMap<>();
        params.put("owner", owner.getFirstName() + " " + owner.getLastName());
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("address", user.getAddress());
        params.put("email", user.getEmail());
        params.put("mobileNo", user.getMobileNo());
        params.put("role", user.getRole());
        params.put("portletUri", portletUri);

        messagingService.sendEmail(owner.getEmail(),
                "New User SignUp", "messages" + File.separator + "approve_user", params);
    }

    private void sendUserSetUpEmail(User user, User owner) {
        Map<String, Object> params = new HashMap<>();
        params.put("owner", owner.getFirstName() + " " + owner.getLastName());
        params.put("user", user.getFirstName() + " " + user.getLastName());
        params.put("role", user.getRole());
        params.put("portletUri", portletUri);
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());

        messagingService.sendEmail(user.getEmail(),
                "New User SignUp", "messages" + File.separator + "welcome_new", params);
    }
}
