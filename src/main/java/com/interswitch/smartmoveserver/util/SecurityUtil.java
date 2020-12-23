package com.interswitch.smartmoveserver.util;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtil {

    @Autowired
    private UserRepository userRepository;

    @Value("${passport.logout.url}")
    private String passportLogoutUrl;

    @Value("${passport.signup-url}")
    private String passportSignUpUrl;

    @Value("${spring.security.oauth2.client.registration.passport.client-id}")
    private String clientId;

    @Value("${smartmove.url}")
    private String smartmoveUrl;


    public boolean isOwner(String username, Long owner){
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
        }
        //TODO:: see below
        //returns true if principal equals owner or is hierarchical parent of owner
        return true;
    }

    public boolean isOwnedEntity(Enum.Role role) {
        return role != Enum.Role.ISW_ADMIN;
    }

    public boolean isOperator(User user) {
        return user.getRole() == Enum.Role.OPERATOR;
    }

    public String getPassportSignUpUrl() {
        return passportSignUpUrl + "?client_id=" + clientId + "&redirect_uri=" + smartmoveUrl + "/signup";
    }

    public String getPassportLogoutUrl() {
        return passportLogoutUrl + "?client_id=" + clientId + "&redirect_uri=" + smartmoveUrl;
    }
}