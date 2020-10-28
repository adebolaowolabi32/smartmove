package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.PageView;
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

    public PageView<Terminal> findAllPaginated(Long owner, int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal);
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.get().getRole())) {
                Page<Terminal> pages = terminalRepository.findAllByOwner(pageable, user.get());
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            else {
                Page<Terminal> pages = terminalRepository.findAll(pageable);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                Optional<User> ownerUser = userRepository.findById(owner);
                if (!ownerUser.isPresent())
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found");
                Page<Terminal> pages = terminalRepository.findAllByOwner(pageable, ownerUser.get());
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public Terminal save(Terminal terminal, String principal) {
        long id = terminal.getId();
        boolean exists = terminalRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Terminal already exists");
        if(terminal.getOwner() == null) {
            User owner = userRepository.findByUsername(principal).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));
            terminal.setOwner(owner);
        }
        return terminalRepository.save(terminal);
    }

    public Terminal findById(long id) {
        return terminalRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Terminal does not exist"));
    }
    
    public Terminal findById(long id, String principal) {
        return terminalRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Terminal does not exist"));
    }

    public Terminal update(Terminal terminal, String principal) {
        Optional<Terminal> existing = terminalRepository.findById(terminal.getId());
        if(existing.isPresent()){
            if(terminal.getOwner() == null) {
                User owner = userRepository.findByUsername(principal).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));
                terminal.setOwner(owner);
            }
            return terminalRepository.save(terminal);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Terminal does not exist");
    }

    public void delete(long id, String principal) {
        Optional<Terminal> existing = terminalRepository.findById(id);
        if(existing.isPresent())
            terminalRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Terminal does not exist");
        }
    }

    public Long countByOwner(User user){
        return terminalRepository.countByOwner(user);
    }

    public Long countAll(){
        return terminalRepository.count();
    }
}
