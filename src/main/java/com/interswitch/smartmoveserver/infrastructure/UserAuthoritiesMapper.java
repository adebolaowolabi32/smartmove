package com.interswitch.smartmoveserver.infrastructure;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.IswCoreService;
import com.interswitch.smartmoveserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class UserAuthoritiesMapper {

    @Autowired
    IswCoreService coreService;

    @Autowired
    UserService userService;

    private Set<GrantedAuthority> getGrantedAuthorities(List<String> permissions) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }

    public GrantedAuthoritiesMapper get() {
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();

        return (authorities) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            if(authentication != null){
                Map<String, Object> principal = (Map<String, Object>)authentication.getPrincipal();
                String username = principal.get("username").toString();
                User user = userService.findByUsername(username);

                authorities.forEach(authority -> {
                    mappedAuthorities.addAll(getGrantedAuthorities(coreService.getPermissions(user)));
                });

            }
            return mappedAuthorities;
        };
    }
}