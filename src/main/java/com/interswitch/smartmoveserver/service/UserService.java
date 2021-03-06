package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.dto.UserDto;
import com.interswitch.smartmoveserver.model.request.ChangePassword;
import com.interswitch.smartmoveserver.model.request.PassportUser;
import com.interswitch.smartmoveserver.model.request.UserLoginRequest;
import com.interswitch.smartmoveserver.model.request.UserRegRequest;
import com.interswitch.smartmoveserver.model.response.UserPassportResponse;
import com.interswitch.smartmoveserver.model.response.UserRoleResponse;
import com.interswitch.smartmoveserver.repository.UserApprovalRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.util.FileParser;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.RandomUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
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
import java.security.Principal;
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

    public String login(UserLoginRequest user) {
        UserPassportResponse response = doUserAuth(user);
        return response != null ? response.getAccessToken() : "";
    }

    @Autowired
    private VerificationTokenService verificationTokenService;

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

    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public User registerUserFromAPI(User user, String principal) {
        boolean exists = userRepository.existsById(user.getId());
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        //TODO :: see below
        //iswCoreService.createUser(user);
        PassportUser passportUser = passportService.findUser(user.getEmail());
        if (passportUser == null) {
            user.setLoginFreqType(1);
            passportUser = passportService.createUser(user);
            saveToDb(passportUser, user, principal);
        } else {
            saveToDb(passportUser, user, principal);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists. Kindly ask user to login with their Quickteller credentials");
        }
        return user;
    }

    private User saveToDb(PassportUser passportUser, User user, String owner) {
        Enum.Role role = user.getRole();
        user.setOwner(null);

        if (!role.equals(Enum.Role.ISW_ADMIN)) {
            Optional<User> ownerUser = userRepository.findByUsername(owner);
            if (ownerUser.isPresent()) user.setOwner(ownerUser.get());
        }

        user.setUsername(passportUser.getUsername());
        user.setPassword(passportUser.getPassword());
        userRepository.save(user);

        if (role.equals(Enum.Role.AGENT)) {
            walletService.autoCreateForUser(user);
            cardService.autoCreateForUser(user);
        }

        return user;
    }

    //if user role is admin, user.owner must be null
    //else must have value
    //if principal is admin user.owner must be populated
    //if not, bounce request
    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public User create(User user, String principal) {
        boolean exists = userRepository.existsByUsername(user.getEmail());
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        User owner = user.getOwner();
        if (owner == null) {
            owner = findByUsername(principal);
        }
        PassportUser passportUser = passportService.findUser(user.getEmail());
        if (passportUser != null) {
            User usr = passportService.buildUser(passportUser);
            user.setUsername(usr.getUsername());
            save(user, owner);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists. Kindly ask user to login with their Quickteller credentials");
        }

        //TODO :: see below
        boolean makerChecker = checkForMakerChecker(user, owner, principal);
        if (makerChecker) {
            user.setUsername(user.getEmail());
            save(user, owner);
            sendMakerCheckerEmail(user, owner);
            UserApproval approval = new UserApproval();
            approval.setOwner(owner);
            approval.setUsr(user);
            approval.setSignUpType(Enum.SignUpType.CREATED_BY_ADMIN);
            userApprovalRepository.save(approval);
            return user;
        }

        //setting default password for all users
        String randomDefaultPassword = new RandomUtil(8).nextString();
        user.setPassword(randomDefaultPassword);
        passportUser = passportService.createUser(user);
        //iswCoreService.createUser(user);
        user.setUsername(passportUser.getUsername());
        save(user, owner);
        sendUserSetUpEmail(user, owner);
        return user;
    }

    private User saveAsAdmin(PassportUser passportUser) {
        User user = passportService.buildUser(passportUser);
        user.setRole(Enum.Role.ISW_ADMIN);
        user.setEnabled(true);
        return save(user, null);
    }

    /**
     * -create the user on passport
     * -create the user on smartmove
     * -send mail for user email verification.
     *
     * @param userRegRequest
     * @return
     */
    public String doSelfSignUp(UserRegRequest userRegRequest) {
        User userReq = userRegRequest.mapUserRequestToUser();
        Optional<User> optionalUser = userRepository.findByEmail(userReq.getEmail());
        User userInSmartMove = null;

        if (optionalUser.isPresent()) {
            userInSmartMove = optionalUser.get();
        }

        if (userInSmartMove != null) {

            UserApproval userApproval = userApprovalRepository.findByUsrId(userInSmartMove.getId());
            if (userApproval != null && userApproval.isApproved()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists.");
            } else if (userApproval != null && !userApproval.isApproved()) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You have already completed this process. You will receive an email with your smartmove credentials as soon as you are approved.");
            } else {
                //send email verification email again
                sendVerificationMail(userInSmartMove);
                return "Your user account exists on smartmove but has not been verified via email.Please check your email for verification link!";
            }
        }
        User owner = null;
        String ownerName = userRegRequest.getOwner();
        if (!ownerName.isEmpty()) {
            Optional<User> ownerOptional = userRepository.findByUsername(ownerName);
            if (ownerOptional.isPresent()) {
                owner = ownerOptional.get();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("The referrer : %s, does not exist on Smartmove ", ownerName));
            }
        }
        PassportUser passportUser = passportService.findUser(userReq.getEmail());
        if (passportUser != null) {
            User usr = passportService.buildUser(passportUser);
            userReq.setUsername(usr.getUsername());
        } else {
            userReq.setLoginFreqType(2);
            passportUser = passportService.createUser(userReq);
            userReq.setUsername(passportUser.getUsername());
        }
        save(userReq, owner);
        sendVerificationMail(userReq);
        String message = String.format("Hi %s, a user verification email has been sent to you.Please kindly check to proceed with your on-boarding process", userReq.getFirstName());
        return message;
    }

    private void sendVerificationMail(User user) {

        VerificationToken verificationToken = verificationTokenService.createToken(user);

        String token = verificationToken.getToken();

        String verificationUrl = portletUri + "/verify?token=" + token;

        Map<String, Object> params = new HashMap<>();
        params.put("name", user.getFirstName());
        params.put("verificationUrl", verificationUrl);
        params.put("portletUri", portletUri);

        messagingService.sendEmail(user.getEmail(),
                "Email Verification", "messages" + File.separator + "email_verification", params);
    }


    private User save(User user, User owner) {
        Enum.Role role = user.getRole();
        user.setOwner(null);
        user.setTillStatus(Enum.TicketTillStatus.OPEN);
        if (!role.equals(Enum.Role.ISW_ADMIN)) {
            user.setOwner(owner);
        }
        if (user.getPicture() != null && user.getPicture().getSize() > 0) {
            Document doc = documentService.saveDocument(new Document(user.getPicture()));
            user.setPictureUrl(doc.getUrl());
        }

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

    public List<User> findAllByRole(String principal, long owner, Enum.Role role) {
        Optional<User> user = userRepository.findByUsername(principal);
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if (owner == 0) {
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
        } else {
            if (securityUtil.isOwnedEntity(role)) {
                if (securityUtil.isOwner(principal, owner)) {
                    Optional<User> ownerUser = userRepository.findById(owner);
                    if (!ownerUser.isPresent())
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found");
                    return userRepository.findAllByRoleAndOwner(role, ownerUser.get());
                }
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
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

    public UserRoleResponse findUserRoleByUsername(String username) {
        User user = findByUsername(username);
        UserRoleResponse userRoleResponse = new UserRoleResponse();
        userRoleResponse.setId(user.getId());
        userRoleResponse.setEmail(user.getEmail());
        userRoleResponse.setOwnerId(user.getOwner() != null ? user.getId() : 0);
        userRoleResponse.setRole(user.getRole() != null ? user.getRole().name() : "");
        return userRoleResponse;
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
            //return userRepository.save(passportService.buildUser(passportUser));
            return null;
            //throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The username you provided does not exist on SmartMove");
        }
        return null;
        //throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to this resource");
    }


    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public User update(User user, String principal) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            User existing = existingUser.get();
            if (user.getPicture().getSize() > 0) {
                Document doc = documentService.saveDocument(new Document(user.getPicture()));
                existing.setPictureUrl(doc.getUrl());
            }
            existing.setAddress(user.getAddress());
            existing.setEnabled(user.isEnabled());
            return userRepository.save(existing);
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
        if (file.getSize() > 1) {
            FileParser<UserDto> fileParser = new FileParser<>();
            List<UserDto> userDtoList = fileParser.parseFileToEntity(file, UserDto.class);
            userDtoList.forEach(userDto -> {
                savedUsers.add(create(mapToUser(userDto, owner), principal));
            });
        }
        return savedUsers.size() > 1;
    }

    private User mapToUser(UserDto userDto, User owner) {

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

    private Enum.Role convertToRoleEnum(String role) {
        //  ISW_ADMIN, REGULATOR, OPERATOR, EXECUTIVE, SERVICE_PROVIDER, INSPECTOR, TICKETER, VEHICLE_OWNER, AGENT, DRIVER, COMMUTER
        return Enum.Role.valueOf(role.toUpperCase());
    }

    private boolean isEnabled(String enabledStatus) {
        return enabledStatus.equalsIgnoreCase("true") || enabledStatus.startsWith("true");
    }

    public List<UserApproval> getApprovals(String principal) {
        List<UserApproval> approvals = new ArrayList<>();
        Optional<User> owner = userRepository.findByUsername(principal);
        if (owner.isPresent()) {
            User user = owner.get();
            if (user.getRole() != Enum.Role.ISW_ADMIN)
                approvals = userApprovalRepository.findAllByOwner(user);
            else
                approvals = userApprovalRepository.findAll();
        }
        return approvals;
    }


    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public boolean approveUser(String principal, long id) {
        Optional<UserApproval> userApproval = userApprovalRepository.findById(id);
        if (userApproval.isPresent()) {
            UserApproval approval = userApproval.get();
            User loggedInUser = findByUsername(principal);
            String owner = approval.getOwner() != null ? approval.getOwner().getUsername() : "";

            if (owner.equals(principal) || loggedInUser.getRole() == Enum.Role.ISW_ADMIN) {
                approval.setApproved(true);
                approval.setDeclined(false);
                if (userApprovalRepository.save(approval).isApproved()) {
                    if (approval.getSignUpType() == Enum.SignUpType.CREATED_BY_ADMIN) {
                        User user = approval.getUsr();
                        user.setPassword(new RandomUtil(8).nextString());
                        PassportUser passportUser = passportService.createUser(user);
                        user.setEnabled(true);
                        if (passportUser != null) userRepository.save(user);
                        sendUserSetUpEmail(user, approval.getOwner());
                    } else if (approval.getSignUpType() == Enum.SignUpType.SELF_SIGNUP) {
                        User user = approval.getUsr();
                        user.setEnabled(true);
                        userRepository.save(user);
                        sendUserSetUpEmail(user, approval.getOwner());
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public boolean declineUser(String principal, long id) {
        Optional<UserApproval> userApproval = userApprovalRepository.findById(id);
        User loggedInUser = findByUsername(principal);
        if (userApproval.isPresent()) {
            UserApproval approval = userApproval.get();
            String owner = approval.getOwner() != null ? approval.getOwner().getUsername() : "";
            if (owner.equals(principal) || loggedInUser.getRole() == Enum.Role.ISW_ADMIN) {
                approval.setDeclined(true);
                approval.setApproved(false);
                if (userApprovalRepository.save(approval).isDeclined()) {
                    User user = approval.getUsr();
                    user.setEnabled(false);
                    userRepository.save(user);
                    sendDeclinedUserEmail(user, approval.getOwner());
                    return true;
                }
                return false;
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
        params.put("user", user.getFirstName() + " " + user.getLastName());
        params.put("role", user.getRole());
        params.put("portletUri", portletUri);
        params.put("username", user.getUsername());


        UserApproval userApproval = userApprovalRepository.findByUsrId(user.getId());

        if (userApproval != null) {

            if (userApproval.getSignUpType() == Enum.SignUpType.CREATED_BY_ADMIN) {
                params.put("password", user.getPassword());
            }
        }

        if (owner == null) {
            messagingService.sendEmail(user.getEmail(),
                    "User SignUp Approved", "messages" + File.separator + "welcome_new", params);
        } else {
            String ownerName = owner.getFirstName() + " " + owner.getLastName();
            params.put("owner", ownerName);
            messagingService.sendEmail(user.getEmail(),
                    "User SignUp Approved", "messages" + File.separator + "welcome_new", params);
        }
    }

    private void sendDeclinedUserEmail(User user, User owner) {

        Map<String, Object> params = new HashMap<>();
        String ownerName = owner != null ? owner.getFirstName() + " " + owner.getLastName() : "Smartmove";
        params.put("owner", ownerName);
        params.put("user", user.getFirstName() + " " + user.getLastName());
        params.put("role", user.getRole());
        params.put("portletUri", portletUri);
        params.put("username", user.getUsername());
        messagingService.sendEmail(user.getEmail(),
                "User SignUp Declined", "messages" + File.separator + "declined_user", params);
    }

    public UserPassportResponse doUserAuth(UserLoginRequest loginRequest) {
        UserPassportResponse passportResponse = null;
        try {
            passportResponse = passportService.getUserAccessDetails(loginRequest);
            if (passportResponse == null) return null;
            User smartMoveUser = findByUsername(passportResponse.getUser_name());
            passportResponse.setRole(smartMoveUser != null ? smartMoveUser.getRole().name() : "");
        } catch (ResponseStatusException ex) {
            if (ex.getStatus() == HttpStatus.FORBIDDEN) {
                String description = ex.getReason();
                if (description.contains("It appears this is your first login. Please change your password"))
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "First login,need to reset password!");
            }

            if (ex.getStatus() == HttpStatus.NOT_FOUND) {
                return passportResponse;
            }
        }
        return passportResponse;
    }

    public VerificationToken getEmailVerificationToken(String token) {

        VerificationToken verificationToken = verificationTokenService.findByToken(token);

        if (verificationToken == null) {
            verificationToken = new VerificationToken();
            verificationToken.setTokenStatus(Enum.EmailVerificationTokenStatus.INVALID);
            return verificationToken;
        }

        User verificationTokenUser = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            verificationToken.setTokenStatus(Enum.EmailVerificationTokenStatus.EXPIRED);
            //initiate call to send another verification link
            sendVerificationMail(verificationTokenUser);
            return verificationToken;
        }

        Optional<User> optionalUser = userRepository.findByEmail(verificationTokenUser.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmailVerified(true);
            userRepository.save(user);
            verificationToken.setUser(user);
        }

        verificationToken.setTokenStatus(Enum.EmailVerificationTokenStatus.VALID);
        //initiate call to approvalRepo
        UserApproval approval = new UserApproval();
        approval.setOwner(verificationTokenUser.getOwner());
        approval.setUsr(verificationTokenUser);
        approval.setSignUpType(Enum.SignUpType.SELF_SIGNUP);
        userApprovalRepository.save(approval);
        if (verificationTokenUser.getOwner() != null) {
            sendMakerCheckerEmail(verificationTokenUser, verificationTokenUser.getOwner());
        }
        return verificationToken;
    }

    public boolean changePassword(Principal principal, ChangePassword changePassword) {
        UserLoginRequest user = new UserLoginRequest();
        user.setUsername(principal.getName());
        user.setPassword(changePassword.getOldPassword());
        UserPassportResponse response = doUserAuth(user);
        String accessToken = response != null ? response.getAccessToken() : "";
        if (accessToken.equals("")) return false;
        passportService.changePassword(accessToken, changePassword);
        return true;
    }
}