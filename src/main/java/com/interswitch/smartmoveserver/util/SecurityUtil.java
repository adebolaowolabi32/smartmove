package com.interswitch.smartmoveserver.util;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Component
public class SecurityUtil {

    @Autowired
    private UserRepository userRepository;

    public boolean isOwner(Principal principal, Long owner){
        Optional<User> userOptional = userRepository.findByUsername(principal.getName());
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
}
