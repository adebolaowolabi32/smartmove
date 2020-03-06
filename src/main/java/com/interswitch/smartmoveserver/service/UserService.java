package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.helper.JwtHelper;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    private JwtHelper jwtHelper;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Page<User> getAllPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return userRepository.findAll(pageable);
    }

    public Page<User> findByRolePaginated(int page, int size, Enum.Role role) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return userRepository.findAllByRole(pageable, role);
    }

    public User save(User user) {
        long id = user.getId();
        boolean exists = userRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");

        passportService.createUser(user);
        iswCoreService.createUser(user);
        return userRepository.save(user);
    }

    public User save(User user, User parent) {
        long id = user.getId();
        boolean exists = userRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        user.setParent(parent);
        passportService.createUser(user);
        iswCoreService.createUser(user);
        return userRepository.save(user);    }

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }

    public List<User> find(Enum.Role role) {
        return userRepository.findAllByRole(role);
    }

    public List<User> findByParent(long parent) {
        Optional<User> user = userRepository.findById(parent);
        if(user.isPresent())
            return userRepository.findAllByParent(user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }

    public List<User> findAllByRoleAndParent(long parent, Enum.Role role) {
        Optional<User> optionalUser = userRepository.findById(parent);
        if(optionalUser.isPresent())
            return userRepository.findAllByRoleAndParent(role, optionalUser.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }
    public User findByMobile(String mobile) {
        return userRepository.findByMobileNo(mobile).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }

    public User update(User user) {
        Optional<User> existing = userRepository.findById(user.getId());
        if(existing.isPresent())
            return userRepository.save(user);
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
   /* public User save(Enum.Role type, long id) {
        User user = new User();
        user.setType(type);//fetch type from id
        user.setParent(id);
        user.setFirstName(jwtHelper.getClaim("firstName"));
        user.setLastName(jwtHelper.getClaim("lastName"));
        user.setEmail(jwtHelper.getClaim("email"));
        user.setMobileNo(jwtHelper.getClaim("mobileNo"));
        return userRepository.save(user);
    }*/

    /*public OnboardUser onBoard(OnboardUser onboardUser) {
        String email = jwtHelper.getClaim("email");
        User user = userRepository.findByEmail(email).get();
        Enum.Role parentType = user != null ? user.getRoles() : null;
        if(parentType != null){
            Enum.Role onboardRole = onboardUser.getType();
            switch (parentType){
                case ISW_ADMIN:
                    break;
                case REGULATOR:
                    if(onboardRole.equals(Enum.Role.AGENT) || onboardRole.equals(Enum.Role.OPERATOR)){
                        // send mail to all
                        break;
                    }
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You are not permitted to create a user of this type");
                case OPERATOR:
                    if(onboardRole.equals(Enum.Role.VEHICLE_OWNER))
                        //send mail to agent
                        break;
                default:
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You are not permitted to create any user");
            }
            *//*onboardUser.getEmail();
            try{
                //notify user's email
            }
            catch (Exception e){
                e.printStackTrace();
            }*//*
        }
        return onboardUser;
    }*/

    public List<User> getAllNext(Enum.Role type, User parent) {
        return userRepository.findAllByRoleAndParent(type, parent);
    }

    public void activate(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setActive(true);
            userRepository.save(user);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
    }

    public void deactivate(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setActive(false);
            userRepository.save(user);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
    }

    public Long countByRole(Enum.Role role){
        return userRepository.countByRole(role);
    }
}
