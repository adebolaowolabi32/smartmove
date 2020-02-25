package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Permission;
import com.interswitch.smartmoveserver.model.Role;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.RoleRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@Transactional
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(), "", user.isActive(), true, true,
                    true, getAuthorities(user.getRoles()));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Set<Role> roles) {

        return getGrantedAuthorities(getPermissions(roles));
    }

    private List<String> getPermissions(Collection<Role> roles) {

        List<String> permissions = new ArrayList<>();
        List<Permission> collection = new ArrayList<>();
        for (Role role : roles) {
            collection.addAll(role.getPermissions());
        }
        for (Permission item : collection) {
            permissions.add(item.getName());
        }
        return permissions;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> permissions) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }
}