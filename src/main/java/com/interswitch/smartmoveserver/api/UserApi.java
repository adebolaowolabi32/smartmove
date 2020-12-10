package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.UserService;
import com.interswitch.smartmoveserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/users")
public class UserApi {

    @Autowired
    private UserService userService;

    @GetMapping(produces = "application/json")
    public PageView<User> findAll(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size) {
        return userService.findAllPaginated(page, size, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private void save(@Validated @RequestBody User user) {
        userService.save(user, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private User findById(@Validated @PathVariable long id) {
        return userService.findById(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        userService.delete(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }
}
