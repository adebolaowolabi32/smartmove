package com.interswitch.smartmoveserver.util;

import com.interswitch.smartmoveserver.model.Enum;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class SecurityUtil {

    public boolean isOwner(Principal principal, Long owner){
        //TODO:: see below
        //returns true if principal equals owner or is hierarchical parent of owner
        return true;
    }

    public boolean isOwnedEntity(Enum.Role role) {
        return role != Enum.Role.ISW_ADMIN;
    }
}
