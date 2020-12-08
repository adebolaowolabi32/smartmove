package com.interswitch.smartmoveserver.audit;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitchng.audit.service.AuditActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditActorServiceImpl implements AuditActorService {

    @Autowired
    private UserService userService;

    @Override
    public String getActor() {
        String actor = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName).get();
        return !actor.isEmpty() ? actor : "Unknown";
    }

    @Override
    public String getActorDomainCode() {
        return null;
    }

    public User getActorUserDetails() {
        String actorUsername = getActor();
        User user;
        try {
            user = userService.findByUsername(actorUsername);
            return user;
        } catch (Exception ex) {
            return null;
        }
    }
}
