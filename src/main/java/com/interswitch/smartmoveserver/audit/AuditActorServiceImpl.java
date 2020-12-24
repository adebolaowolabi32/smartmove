package com.interswitch.smartmoveserver.audit;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.util.JwtUtil;
import com.interswitchng.audit.service.AuditActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class AuditActorServiceImpl implements AuditActorService {

    @Autowired
    private UserService userService;

    @Override
    public String getActor() {
        return JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication());

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
