package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Page;
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
    public Page<User> findAll(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size, Principal principal) {
        org.springframework.data.domain.Page pageable = userService.findAllPaginated(principal, page, size);
        return new Page<User>(pageable.getTotalElements(), pageable.getContent());
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private void save(@Validated @RequestBody User user, Principal principal) {
        userService.save(user, principal);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private User findById(@Validated @PathVariable long id, Principal principal) {
        return userService.findById(id, principal);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id, Principal principal) {
        userService.delete(id, principal);
    }
}
