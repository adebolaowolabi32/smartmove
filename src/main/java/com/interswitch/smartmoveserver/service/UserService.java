package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.helper.JwtHelper;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.request.OnboardUser;
import com.interswitch.smartmoveserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtHelper jwtHelper;

    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        long id = user.getId();
        boolean exists = userRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        return userRepository.save(user);
    }

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }

    public Iterable<User> find(Enum.UserType type) {
        return userRepository.findAllByType(type);
    }

    public Iterable<User> findByParent(long parent) {
        Optional<User> user = userRepository.findById(parent);
        if(user.isPresent())
            return userRepository.findAllByParent(user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
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
   /* public User save(Enum.UserType type, long id) {
        User user = new User();
        user.setType(type);//fetch type from id
        user.setParent(id);
        user.setFirstName(jwtHelper.getClaim("firstName"));
        user.setLastName(jwtHelper.getClaim("lastName"));
        user.setEmail(jwtHelper.getClaim("email"));
        user.setMobileNo(jwtHelper.getClaim("mobileNo"));
        return userRepository.save(user);
    }*/

    public OnboardUser onBoard(OnboardUser onboardUser) {
        String email = jwtHelper.getClaim("email");
        User user = userRepository.findByEmail(email).get();
        Enum.UserType parentType = user != null ? user.getType() : null;
        if(parentType != null){
            Enum.UserType onboardUserType = onboardUser.getType();
            switch (parentType){
                case ISW_ADMIN:
                    break;
                case REGULATOR:
                    if(onboardUserType.equals(Enum.UserType.AGENT) || onboardUserType.equals(Enum.UserType.OPERATOR)){
                        // send mail to all
                        break;
                    }
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You are not permitted to create a user of this type");
                case OPERATOR:
                    if(onboardUserType.equals(Enum.UserType.BUS_OWNER))
                        //send mail to agent
                        break;
                default:
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You are not permitted to create any user");
            }
            /*onboardUser.getEmail();
            try{
                //notify user's email
            }
            catch (Exception e){
                e.printStackTrace();
            }*/
        }
        return onboardUser;
    }

    public Iterable<User> getAllNext(Enum.UserType type) {
        String email = jwtHelper.getClaim("email");
        User user = userRepository.findByEmail(email).get();
        long parentId = user != null ? user.getId() : 0;

        if(parentId != 0){
            return userRepository.findAllByTypeAndParentId(type, parentId);
        }
        return new ArrayList<>();
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
}
