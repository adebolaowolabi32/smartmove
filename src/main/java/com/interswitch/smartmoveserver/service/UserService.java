package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.helper.JwtHelper;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * @author adebola.owolabi
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PassportService passportService;

    @Autowired
    IswCoreService iswCoreService;

    @Autowired
    WalletService walletService;

    @Autowired
    CardService cardService;
    @Autowired
    private JwtHelper jwtHelper;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Page<User> getAllPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return userRepository.findAll(pageable);
    }

    public Page<User> findByRolePaginated(int page, int size, Enum.Role role) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return userRepository.findAllByRole(pageable, role);
    }

    public void save(User user) {
        Optional<User> exists = userRepository.findByUsername(user.getUsername());
        if (exists.isPresent()) return;
        //passportService.createUser(user);
        //iswCoreService.createUser(user);
        else userRepository.save(user);
    }

    public User save(User user, Principal principal) {
        boolean exists = userRepository.existsById(user.getId());
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        Enum.Role role = user.getRole();
        user.setParent(null);
        if(!role.equals(Enum.Role.ISW_ADMIN)){
            Optional<User> parent = userRepository.findByUsername(principal.getName());
            if(parent.isPresent()) user.setParent(parent.get());
        }
        //passportService.createUser(user);
        user.setUsername(user.getEmail());
        user.setPassword(null);
        //iswCoreService.createUser(user);
        userRepository.save(user);
        if(role.equals(Enum.Role.AGENT)){
            walletService.autoCreateForUser(user);
            cardService.autoCreateForUser(user);
        }
        return user;
    }

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }

    public List<User> find(Enum.Role role) {
        return userRepository.findAllByRole(role);
    }

    public List<User> findByParent(long parent) {
        Optional<User> user = userRepository.findById(parent);
        if(user.isPresent())
            return userRepository.findAllByParent(user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }

    public List<User> findAllByRoleAndParent(long parent, Enum.Role role) {
        Optional<User> optionalUser = userRepository.findById(parent);
        if(optionalUser.isPresent())
            return userRepository.findAllByRoleAndParent(role, optionalUser.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }

    public List<User> findAllByRole(Enum.Role role) {
        return userRepository.findAllByRole(role);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }
    public User findByMobile(String mobile) {
        return userRepository.findByMobileNo(mobile).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
    }

    public User update(User user) {
        Optional<User> existing = userRepository.findById(user.getId());
        if(existing.isPresent())
        {
            user.setUsername(user.getEmail());
            return userRepository.save(user);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
    }

    public void delete(long id) {
        Optional<User> existing = userRepository.findById(id);
        if(existing.isPresent())
            userRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }
    }

    public List<User> getAllNext(Enum.Role type, User parent) {
        return userRepository.findAllByRoleAndParent(type, parent);
    }

    public void activate(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setEnabled(true);
            userRepository.save(user);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
    }

    public void deactivate(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setEnabled(false);
            userRepository.save(user);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
    }

    public Long countByRole(Enum.Role role){
        return userRepository.countByRole(role);
    }

    public Long countByRoleAndParent(Enum.Role role, User parent){
        return userRepository.countByRoleAndParent(role, parent);
    }
}
