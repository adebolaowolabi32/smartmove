package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.ViewResponse;
import com.interswitch.smartmoveserver.model.request.PassportUser;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * @author adebola.owolabi
 */
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
            PassportUser passportUser = passportService.findUserByUsername(user.getUsername());
            if(passportUser == null) passportUser = passportService.createUser(user);
            user.setUsername(passportUser.getUsername());
            user.setPassword(passportUser.getPassword());
            //iswCoreService.createUser(user);
            userRepository.save(user);
        }
    }

    //if user role is admin, user.owner must be null
    //else must have value
    //if principal is admin user.owner must be populated
    //if not, bounce request

    public User save(User user, Principal principal) {
        ViewResponse response = new ViewResponse();
        boolean exists = userRepository.existsById(user.getId());
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        //iswCoreService.createUser(user);
        PassportUser passportUser = passportService.findUserByUsername(user.getEmail());
        if(passportUser == null) {
            passportUser = passportService.createUser(user);
            save(passportUser, user, principal);
            response.setMessage(user.getRole().toString().toLowerCase() + " saved successfully");
        }
        else{
            save(passportUser, user, principal);
            response.setError("User already exists. Kindly ask user to login with their Quickteller credentials");
        }
        return user;
    }

    private User save(PassportUser passportUser, User user, Principal principal){
        Enum.Role role = user.getRole();
        user.setOwner(null);
        if(!role.equals(Enum.Role.ISW_ADMIN)) {
            Optional<User> owner = userRepository.findByUsername(principal.getName());
            if(owner.isPresent()) user.setOwner(owner.get());
        }
        user.setUsername(passportUser.getUsername());
        user.setPassword(passportUser.getPassword());
        userRepository.save(user);
        if(role.equals(Enum.Role.AGENT)){
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
        if(user.isPresent())
            return userRepository.findAllByOwner(user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }

    public List<User> findAllByRole(Enum.Role role) {
        return userRepository.findAllByRole(role);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }

    public User findOrCreateUser(Principal principal) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if(user.isPresent()) {
            return user.get();
        }
        PassportUser passportUser = passportService.findUserByUsername(principal.getName());
        if(passportUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in principal does not exist");
        }
        else{
            User user1 = new User();
            //TODO:: change ISW_ADMIN
            user1.setRole(Enum.Role.ISW_ADMIN);
            return save(passportUser, user1, principal);
        }
    }

    public User update(User user) {
        Optional<User> existing = userRepository.findById(user.getId());
        if(existing.isPresent())
        {
            User existingUser = existing.get();
            user.setRole(existingUser.getRole());
            user.setOwner(existingUser.getOwner());
            user.setPassword(null);
            //TODO:: Passport update user requires user credentials
           /* PassportUser passportUser = passportService.updateUser(user);
            user.setUsername(passportUser.getUsername());*/
            return userRepository.save(user);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
    }

    public void delete(long id) {
        Optional<User> existing = userRepository.findById(id);
        if(existing.isPresent())
            userRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }
    }

    public void activate(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setEnabled(true);
            userRepository.save(user);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
    }

    public void deactivate(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setEnabled(false);
            userRepository.save(user);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
    }

    public Long countByRole(Enum.Role role){
        return userRepository.countByRole(role);
    }

    public Long countByRoleAndOwner(User user, Enum.Role role){
        return userRepository.countByRoleAndOwner(role, user);
    }

    public Page<User> findAllByRole(Principal principal, long owner, Enum.Role role, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if(!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist");

            return userRepository.findAllByRole(pageable, role);


      /*  if(owner == 0) {
            return userRepository.findAllByRoleAndOwner(pageable, role, user.get());
        }
        else {
            if(securityUtil.isOwnedEntity(role))
                if(securityUtil.isOwner(principal, owner)){
                    return userRepository.findAllByRoleAndOwner(pageable, role, user.get());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }*/
    }
    public User findById(Principal principal, long id) {
        if(securityUtil.isOwner(principal, id))
            return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
    }

}
