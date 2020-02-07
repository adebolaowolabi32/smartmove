package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.helper.JwtHelper;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.request.OnboardUser;
import com.interswitch.smartmoveserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtHelper jwtHelper;

    public User save(User.UserType type, String id) {
        User user = new User();
        user.setType(type);//fetch type from id
        user.setParent(id);
        user.setFirstName(jwtHelper.getClaim("firstName"));
        user.setLastName(jwtHelper.getClaim("lastName"));
        user.setEmailAddress(jwtHelper.getClaim("email"));
        user.setMobileNumber(jwtHelper.getClaim("mobileNo"));
        return userRepository.save(user);
    }

    public OnboardUser onBoard(OnboardUser onboardUser) {
        String email = jwtHelper.getClaim("email");
        User user = userRepository.findByEmailAddress(email);
        User.UserType parentType = user != null ? user.getType() : null;
        if(parentType != null){
            User.UserType onboardUserType = onboardUser.getType();
            switch (parentType){
                case ISW_ADMIN:
                    break;
                case REGULATOR:
                    if(onboardUserType.equals(User.UserType.AGENT) || onboardUserType.equals(User.UserType.OPERATOR)){
                        // send mail to all
                        break;
                    }
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You are not permitted to create a user of this type");
                case OPERATOR:
                    if(onboardUserType.equals(User.UserType.BUS_OWNER))
                        //send mail to agent
                        break;
                default:
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You are not permitted to create any user");
            }
            /*onboardUser.getEmailAddress();
            try{
                //notify user's email
            }
            catch (Exception e){
                e.printStackTrace();
            }*/
        }
        return onboardUser;
    }

    public List<User> getAll(User.UserType type) {
        String email = jwtHelper.getClaim("email");
        User user = userRepository.findByEmailAddress(email);
        String parentId = user != null ? user.getId() : "";

        if(!parentId.isEmpty()){
            return userRepository.findAllByTypeAndParent(type, parentId);
        }
        return new ArrayList<>();
    }
}
