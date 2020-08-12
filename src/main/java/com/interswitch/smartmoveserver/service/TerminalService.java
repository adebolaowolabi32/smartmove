package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.TerminalRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
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
public class TerminalService {
    @Autowired
    TerminalRepository terminalRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<Terminal> findAll() {
        return terminalRepository.findAll();
    }

    public Page<Terminal> findAllPaginated(Principal principal, Long owner, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.get().getRole()))
                return terminalRepository.findAllByOwner(pageable, user.get());
            else
                return terminalRepository.findAll(pageable);
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                Optional<User> ownerUser = userRepository.findById(owner);
                if (!ownerUser.isPresent())
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found");
                return terminalRepository.findAllByOwner(pageable, ownerUser.get());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public Terminal save(Terminal terminal) {
        long id = terminal.getId();
        boolean exists = terminalRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Terminal already exists");
        return terminalRepository.save(terminal);
    }

    public Terminal save(Terminal terminal, Principal principal) {
        long id = terminal.getId();
        boolean exists = terminalRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Terminal already exists");
        if(terminal.getOwner() == null) {
            Optional<User> owner = userRepository.findByUsername(principal.getName());
            if(owner.isPresent()) terminal.setOwner(owner.get());
        }
        return terminalRepository.save(terminal);
    }
    
    public Terminal findById(long id) {
        return terminalRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Terminal does not exist"));
    }

    public List<Terminal> findByOwner(long owner) {
        Optional<User> user = userRepository.findById(owner);
        if(user.isPresent())
            return terminalRepository.findAllByOwner(user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }

    public Terminal update(Terminal terminal) {
        Optional<Terminal> existing = terminalRepository.findById(terminal.getId());
        if(existing.isPresent())
            return terminalRepository.save(terminal);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Terminal does not exist");
    }

    public void delete(long id) {
        Optional<Terminal> existing = terminalRepository.findById(id);
        if(existing.isPresent())
            terminalRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Terminal does not exist");
        }
    }

    public void activate(long terminalId) {
        Optional<Terminal> terminalOptional = terminalRepository.findById(terminalId);
        if(terminalOptional.isPresent()){
            Terminal terminal = terminalOptional.get();
            terminal.setEnabled(true);
            terminalRepository.save(terminal);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Terminal does not exist");
    }

    public void deactivate(long terminalId) {
        Optional<Terminal> terminalOptional = terminalRepository.findById(terminalId);
        if(terminalOptional.isPresent()){
            Terminal terminal = terminalOptional.get();
            terminal.setEnabled(false);
            terminalRepository.save(terminal);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Terminal does not exist");
    }

    public Long countByOwner(User user){
        return terminalRepository.countByOwner(user);
    }

    public Long countAll(){
        return terminalRepository.count();
    }
}
