package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/users")
public class UserApi {

    @Autowired
    private UserService userService;

/*    @PostMapping(value = "/{type}/{id}", consumes = "application/json", produces = "application/json")
    public User save(@Validated @PathVariable Enum.Role type, @Validated @PathVariable long id){
          return userService.save(type, id);
    }*/
    
    @GetMapping(produces = "application/json")
    public List<User> getAll() {
      return userService.findAll();
    }

    /*@PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private void save(@Validated @RequestBody User user) {
        userService.save(user);
    }*/
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
        User.Role ownerType = user != null ? user.getType() : null;

        if(ownerType != null && ownerType.equals(User.Role.ISW_ADMIN)){
            return userRepository.findAllByType(User.Role.AGENT);
        }
        return Collections.emptyList();
    }*/

   /* @GetMapping(value = "/{type}", produces = "application/json")
    public List<User> getDetails(@Validated @PathVariable User.Role type, @Validated @PathVariable long id) {
        String email = jwtHelper.getClaim("email");
        User user = userRepository.findByEmail(email);
        long ownerId = user != null ? user.getId() : 0;

        if(!ownerId.isEmpty()){
            return userRepository.findAllByTypeAndOwner(type, ownerId);
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
