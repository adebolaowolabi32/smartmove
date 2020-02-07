package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.request.OnboardUser;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.helper.JwtHelper;
import com.interswitch.smartmoveserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/{type}/{id}", consumes = "application/json", produces = "application/json")
    public User save(@Validated @PathVariable User.UserType type, @Validated @PathVariable String id){
          return userService.save(type, id);
    }

    @PostMapping(value = "/register", produces = "application/json")
    public OnboardUser onBoard(@Validated OnboardUser onboardUser){
       return userService.onBoard(onboardUser);
    }

    @GetMapping(value = "/{type}", produces = "application/json")
    public List<User> getAll(@Validated @PathVariable User.UserType type) {
      return userService.getAll(type);
    }

   /* @GetMapping(value = "/{id}", produces = "application/json")
    public List<User> getByType(@Validated @PathVariable String id) {
        String email = jwtHelper.getClaim("email");
        User user = userRepository.findByEmailAddress(email);
        User.UserType parentType = user != null ? user.getType() : null;

        if(parentType != null && parentType.equals(User.UserType.ISW_ADMIN)){
            return userRepository.findAllByType(User.UserType.AGENT);
        }
        return Collections.emptyList();
    }*/

   /* @GetMapping(value = "/{type}", produces = "application/json")
    public List<User> getDetails(@Validated @PathVariable User.UserType type, @Validated @PathVariable String id) {
        String email = jwtHelper.getClaim("email");
        User user = userRepository.findByEmailAddress(email);
        String parentId = user != null ? user.getId() : "";

        if(!parentId.isEmpty()){
            return userRepository.findAllByTypeAndParent(type, parentId);
        }
        return Collections.emptyList();
    }*/
}
