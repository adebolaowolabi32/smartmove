package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.repository.RouteRepository;
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
public class RouteService {
    @Autowired
    RouteRepository routeRepository;

    @Autowired
    TerminalService terminalService;

    @Autowired
    UserService userService;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    public PageView<Route> findAllPaginated(Long owner, int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        User user = userService.findByUsername(principal);
        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                Page<Route> pages = routeRepository.findAllByOwner(pageable, user);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            else {
                Page<Route> pages = routeRepository.findAll(pageable);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                Page<Route> pages = routeRepository.findAllByOwner(pageable, ownerUser);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }


    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Route save(Route route) {
        long id = route.getId();
        boolean exists = routeRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Route already exists");
        return routeRepository.save(route);
    }


    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Route save(Route route, String principal) {
        String name = route.getName();
        Terminal startTerminal = terminalService.findById(route.getStartTerminalId());
        Terminal stopTerminal = terminalService.findById(route.getStopTerminalId());
        String startTerminalName = startTerminal.getName();
        String stopTerminalName = stopTerminal.getName();
        route.setStartTerminalName(startTerminalName);
        route.setStopTerminalName(stopTerminalName);
        route.setName(startTerminalName + " - " + stopTerminalName);
        boolean exists = routeRepository.existsByName(name);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Route with name: " + name + " already exists");
        if (route.getOwner() == null) {
            User owner = userService.findByUsername(principal);
            route.setOwner(owner);
        }
        return routeRepository.save(route);
    }

    public Route findById(long id, String principal) {
        return routeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route does not exist"));
    }

    public List<Route> findByType(Enum.TransportMode type) {
        return routeRepository.findAllByType(type);
    }


    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Route update(Route route, String principal) {
        Optional<Route> existing = routeRepository.findById(route.getId());
        if (existing.isPresent()) {
            Terminal startTerminal = terminalService.findById(route.getStartTerminalId());
            Terminal stopTerminal = terminalService.findById(route.getStopTerminalId());
            String startTerminalName = startTerminal.getName();
            String stopTerminalName = stopTerminal.getName();
            route.setStartTerminalName(startTerminalName);
            route.setStopTerminalName(stopTerminalName);
            route.setName(startTerminalName + " - " + stopTerminalName);
            if (route.getOwner() == null) {
                User owner = userService.findByUsername(principal);
                route.setOwner(owner);
            }
            return routeRepository.save(route);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route does not exist");
    }

    public void delete(long id, String principal) {
        Optional<Route> existing = routeRepository.findById(id);
        if (existing.isPresent())
            routeRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route does not exist");
        }
    }

    public Long countByOwner(User user) {
        return routeRepository.countByOwner(user);
    }

    public Long countAll() {
        return routeRepository.count();
    }
}
