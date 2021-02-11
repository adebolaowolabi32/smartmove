package com.interswitch.smartmoveserver.helper;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.service.IswCoreService;
import com.interswitch.smartmoveserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/*
 * Created by adebola.owolabi on 2/11/2021
 */
@Service(value = "userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    IswCoreService coreService;
    @Autowired
    UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user));
        } else {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
    }

    private Set<GrantedAuthority> getGrantedAuthorities(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        //User user = userService.findOrCreateByUsername(username);
        List<String> permissions = coreService.getPermissions(user);

        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }
}