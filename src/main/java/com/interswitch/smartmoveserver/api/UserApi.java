package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/users")
public class UserApi {

    @Autowired
    private UserService userService;
    
    @GetMapping(produces = "application/json")
    public List<User> findAll() {
      return userService.findAll();
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private User save(Principal principal, @Validated @RequestBody User user) {
        return userService.save(user, principal);
    }
    @GetMapping(value = "/{id}", produces = "application/json")
    private User findById(@Validated @PathVariable long id) {
        return userService.findById(id);
    }

    @GetMapping(value = "/findByOwner/{ownerId}", produces = "application/json")
    private List<User> findByOwner(@Validated @PathVariable long owner) {
        return userService.findByOwner(owner);
    }

    @GetMapping(value = "/findByType/{type}", produces = "application/json")
    private List<User> find(@Validated @PathVariable Enum.Role type) {
        return userService.find(type);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        userService.delete(id);
    }

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
