package com.interswitch.smartmoveserver.handler;

import com.interswitch.smartmoveserver.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserAuthoritiesMapper {

    @Autowired
    CoreService coreService;

    private Set<GrantedAuthority> getGrantedAuthorities(List<String> permissions) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }

    public GrantedAuthoritiesMapper get() {
        //SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                mappedAuthorities.addAll(getGrantedAuthorities(coreService.getUserPermissions()));
            });

            return mappedAuthorities;
        };
    }
}