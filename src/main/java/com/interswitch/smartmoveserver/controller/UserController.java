package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

/*    @PostMapping(value = "/{type}/{id}", consumes = "application/json", produces = "application/json")
    public User save(@Validated @PathVariable Enum.UserType type, @Validated @PathVariable long id){
          return userService.save(type, id);
    }*/
    
    @GetMapping(produces = "application/json")
    public Iterable<User> getAll() {
      return userService.getAll();
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private User save(@Validated @RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private User findById(@Validated @PathVariable long id) {
        return userService.findById(id);
    }

    @GetMapping(value = "/findByParent/{parentId}", produces = "application/json")
    private Iterable<User> findByParent(@Validated @PathVariable long parent) {
        return userService.findByParent(parent);
    }

    @GetMapping(value = "/findByType/{type}", produces = "application/json")
    private Iterable<User> find(@Validated @PathVariable Enum.UserType type) {
        return userService.find(type);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private User update(@Validated @RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        userService.delete(id);
    }

   /* @GetMapping(value = "/{id}", produces = "application/json")
    public List<User> getByType(@Validated @PathVariable long id) {
        String email = jwtHelper.getClaim("email");
        User user = userRepository.findByEmail(email);
        User.UserType parentType = user != null ? user.getType() : null;

        if(parentType != null && parentType.equals(User.UserType.ISW_ADMIN)){
            return userRepository.findAllByType(User.UserType.AGENT);
        }
        return Collections.emptyList();
    }*/

   /* @GetMapping(value = "/{type}", produces = "application/json")
    public List<User> getDetails(@Validated @PathVariable User.UserType type, @Validated @PathVariable long id) {
        String email = jwtHelper.getClaim("email");
        User user = userRepository.findByEmail(email);
        long parentId = user != null ? user.getId() : 0;

        if(!parentId.isEmpty()){
            return userRepository.findAllByTypeAndParent(type, parentId);
        }
        return Collections.emptyList();
    }*/

    @PostMapping(value = "/activate/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void activate(@Validated @PathVariable long id) {
        userService.activate(id);
    }

    @PostMapping(value = "/deactivate/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void deactivate(@Validated @PathVariable long id) {
        userService.deactivate(id);
    }
}
