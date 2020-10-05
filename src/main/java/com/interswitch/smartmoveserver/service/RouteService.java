package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.repository.RouteRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.VehicleRepository;
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
public class RouteService {
    @Autowired
    RouteRepository routeRepository;

    @Autowired
    TerminalService terminalService;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    public Page<Route> findAllPaginated(Principal principal, Long owner, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.get().getRole()))
                return routeRepository.findAllByOwner(pageable, user.get());
            else
                return routeRepository.findAll(pageable);
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                Optional<User> ownerUser = userRepository.findById(owner);
                if (!ownerUser.isPresent())
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found");
                return routeRepository.findAllByOwner(pageable, ownerUser.get());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public Route save(Route route) {
        long id = route.getId();
        boolean exists = routeRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Route already exists");
        return routeRepository.save(route);
    }

    public Route save(Route route, Principal principal) {
        long id = route.getId();
        Terminal startTerminal = terminalService.findById(route.getStartTerminalId());
        Terminal stopTerminal = terminalService.findById(route.getStopTerminalId());
        String startTerminalName = startTerminal.getName();
        String stopTerminalName = stopTerminal.getName();
        route.setStartTerminalName(startTerminalName);
        route.setStopTerminalName(stopTerminalName);
        route.setName(startTerminalName + " - " + stopTerminalName);
        boolean exists = routeRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Route already exists");
        if (route.getOwner() == null) {
            User owner = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));
            route.setOwner(owner);
        }
        return routeRepository.save(route);
    }

    public Route findById(long id, Principal principal) {
        return routeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route does not exist"));
    }

    public List<Route> findByType(Enum.TransportMode type) {
        return routeRepository.findAllByType(type);
    }

    public Route update(Route route, Principal principal) {
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
                User owner = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));
                route.setOwner(owner);
            }
            return routeRepository.save(route);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route does not exist");
    }

    public void delete(long id, Principal principal) {
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
