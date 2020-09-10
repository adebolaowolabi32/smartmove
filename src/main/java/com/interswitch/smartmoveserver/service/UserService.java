package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Document;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.request.PassportUser;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static com.interswitch.smartmoveserver.helper.JwtHelper.isInterswitchEmail;

/**
 * @author adebola.owolabi
 */
@Service
public class UserService {

    private final Log logger = LogFactory.getLog(getClass());

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
    private PageUtil pageUtil;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Page<User> findAll(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return userRepository.findAll(pageable);
    }

    public Page<User> findAllByRole(int page, int size, Enum.Role role) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return userRepository.findAllByRole(pageable, role);
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

    public User save(User user, Principal principal) {
        logger.info("Inside user service...");
        boolean exists = userRepository.existsById(user.getId());
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        //TODO :: see below
        user.setEnabled(true);
        //iswCoreService.createUser(user);
        PassportUser passportUser = passportService.findUser(user.getEmail());
        if (passportUser == null) {
            passportUser = passportService.createUser(user);
            save(passportUser, user, principal.getName());
        } else {
            save(passportUser, user, principal.getName());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists. Kindly ask user to login with their Quickteller credentials");
        }
        return user;
    }

    private User saveAsAdmin(PassportUser passportUser) {
        User user = passportService.buildUser(passportUser);
        user.setRole(Enum.Role.ISW_ADMIN);
        user.setEnabled(true);
        return save(passportUser, user, null);
    }

    private User save(PassportUser passportUser, User user, String owner) {
        logger.info("Inside save for repo save");
        Enum.Role role = user.getRole();
        user.setOwner(null);

        if (!role.equals(Enum.Role.ISW_ADMIN)) {
            Optional<User> ownerUser = userRepository.findByUsername(owner);
            if (ownerUser.isPresent()) user.setOwner(ownerUser.get());
        }

        user.setUsername(passportUser.getUsername());
        user.setPassword(passportUser.getPassword());

        if (!user.getPicture().isEmpty() || user.getPicture().getSize()>0) {
            Document doc = documentService.saveDocument(new Document(user.getPicture()));
            user.setPictureUrl(doc.getUrl());
        }

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

    public List<User> find(Enum.Role role) {
        return userRepository.findAllByRole(role);
    }

    public List<User> findByOwner(long owner) {
        Optional<User> user = userRepository.findById(owner);
        if (user.isPresent())
            return userRepository.findAllByOwner(user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }

    public List<User> findAllByRole(Enum.Role role) {
        return userRepository.findAllByRole(role);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
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

    public User update(long id, boolean enabled) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEnabled(enabled);

            if (user.getPicture()!=null && (!user.getPicture().isEmpty() || user.getPicture().getSize()>0)) {
                Document doc = documentService.saveDocument(new Document(user.getPicture()));
                user.setPictureUrl(doc.getUrl());
            }
            return userRepository.save(user);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
    }

    public void delete(long id) {
        Optional<User> existing = userRepository.findById(id);
        if (existing.isPresent())
            userRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }
    }

    public void activate(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEnabled(true);
            userRepository.save(user);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
    }

    public void deactivate(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEnabled(false);
            userRepository.save(user);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
    }

    public Long countByRole(Enum.Role role) {
        return userRepository.countByRole(role);
    }

    public Long countByRoleAndOwner(User user, Enum.Role role) {
        return userRepository.countByRoleAndOwner(role, user);
    }

    public Long countByRole(Principal principal, User owner, Enum.Role role) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
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

    public Page<User> findAllByRole(Principal principal, long owner, Enum.Role role, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(role)) {
                if (securityUtil.isOwnedEntity(user.get().getRole()))
                    return userRepository.findAllByRoleAndOwner(pageable, role, user.get());
                else
                    return userRepository.findAllByRole(pageable, role);
            } else {
                if (securityUtil.isOwnedEntity(user.get().getRole()))
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
                return userRepository.findAllByRole(pageable, role);
            }
        } else {
            if (securityUtil.isOwnedEntity(role)) {
                if (securityUtil.isOwner(principal, owner)) {
                    Optional<User> ownerUser = userRepository.findById(owner);
                    if (!ownerUser.isPresent())
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found");
                    return userRepository.findAllByRoleAndOwner(pageable, role, ownerUser.get());
                }
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public User findById(Principal principal, long id) {
        if (securityUtil.isOwner(principal, id))
            return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
    }
}
