package com.interswitch.smartmoveserver.audit;

import com.interswitchng.audit.service.AuditActorService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class AuditActorServiceImpl implements AuditActorService {

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
}
