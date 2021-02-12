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


    public boolean isOwner(String username, Long owner) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
        }
        //TODO:: see below
        //returns true if principal equals owner or is hierarchical parent of owner
        return true;
    }

    public boolean isOwnedEntity(Enum.Role role) {
        boolean isOwned = false;
        switch (role) {
            case ISW_ADMIN:
                isOwned = false;
                break;
            case EXECUTIVE:
            case REGULATOR:
            case OPERATOR:
            case VEHICLE_OWNER:
            case AGENT:
            case DRIVER:
            case SERVICE_PROVIDER:
            case INSPECTOR:
            case TICKETER:
            case COMMUTER:
            default:
                isOwned = true;
                break;
        }
        return isOwned;
    }

    public boolean isOperator(User user) {
        return user.getRole() == Enum.Role.OPERATOR;
    }

    public String getPassportSignUpUrl() {
        return passportSignUpUrl + "?client_id=" + clientId + "&redirect_uri=" + smartmoveUrl + "/signup";
    }

    public String getSmartmoveSignupUrl() {
        return smartmoveUrl + "/signupnew";
    }

    public String getSmartmoveLoginUrl() {
        return smartmoveUrl + "/login";
    }

    public String getPassportLogoutUrl() {
        return passportLogoutUrl + "?client_id=" + clientId + "&redirect_uri=" + smartmoveUrl;
    }

    public boolean isPasswordPolicyCompliant(String password) {
        boolean hasSpecialCharacters = password.contains("@") || password.contains("#") || password.contains("$")
                || password.contains("%") || password.contains("!");

        boolean hasAtLeastOneUpperCase = true;

        boolean hastAtLeastOneLowerCase = true;

        boolean hasAtLeastOneDigit = true;

        boolean hasAtLeastSevenCharacters = password.trim().length() >= 7;

        return hasSpecialCharacters && hasAtLeastOneUpperCase && hastAtLeastOneLowerCase &&
                hasAtLeastSevenCharacters && hastAtLeastOneLowerCase;

    }
}