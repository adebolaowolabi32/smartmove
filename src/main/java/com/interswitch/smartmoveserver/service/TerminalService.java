package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.GenericModel;
import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.TerminalRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
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
    UserService userService;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<Terminal> findAll() {
        return terminalRepository.findAll();
    }

    public List<Terminal> findByOwner(String username) {
        User owner = userService.findByUsername(username);
        return terminalRepository.findAllByOwner(owner);
    }

    public PageView<Terminal> findAllPaginated(Long owner, int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        User user = userService.findByUsername(principal);
        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                Page<Terminal> pages = terminalRepository.findAllByOwner(pageable, user);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            else {
                Page<Terminal> pages = terminalRepository.findAll(pageable);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                Page<Terminal> pages = terminalRepository.findAllByOwner(pageable, ownerUser);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }


    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Terminal save(Terminal terminal, String principal) {
        String name = terminal.getName();
        boolean exists = terminalRepository.existsByName(name);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Terminal with name: " + name + " already exists");
        if(terminal.getOwner() == null) {
            User owner = userService.findByUsername(principal);
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

    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Terminal update(Terminal terminal, String principal) {
        Optional<Terminal> existing = terminalRepository.findById(terminal.getId());
        if(existing.isPresent()){
            if(terminal.getOwner() == null) {
                User owner = userService.findByUsername(principal);
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

    @Audited(auditableAction = AuditableAction.DELETE, auditableActionClass = AuditableActionStatusImpl.class)
    public Terminal auditedDelete(long id, String principal) {
        Optional<Terminal> existing = terminalRepository.findById(id);
        if(existing.isPresent()) {
            terminalRepository.deleteById(id);
            return new GenericModel<Terminal>(existing.get()).getEntity();
        }
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
