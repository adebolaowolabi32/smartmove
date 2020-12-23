package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Enum;
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
    public PageView<User> findAll(@RequestParam("role") Enum.Role role, @RequestParam(required = false, defaultValue = "0") Long owner, @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        return userService.findAllPaginatedByRole(JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()), owner, role, page, size);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private void create(@Validated @RequestBody User user) {
        userService.registerUserFromAPI(user, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private User findById(@Validated @PathVariable long id) {
        return userService.findById(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    private void delete(@Validated @PathVariable long id) {
        userService.delete(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }
}