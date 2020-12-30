package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.dto.RouteDto;
import com.interswitch.smartmoveserver.repository.RouteRepository;
import com.interswitch.smartmoveserver.repository.ScheduleRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

    @Autowired
    ScheduleRepository scheduleRepository;

    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    public List<Route> findAll(Long owner, String principal) {
        User user = userService.findByUsername(principal);
        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                return routeRepository.findAllByOwner(user);
            } else {
                return routeRepository.findAll();
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                return routeRepository.findAllByOwner(ownerUser);
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
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
        String startTerminalName = route.getStartTerminal().getName();
        String stopTerminalName = route.getStopTerminal().getName();
        String name = startTerminalName + " - " + stopTerminalName;
        route.setName(name);
        boolean exists = routeRepository.existsByName(name);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Route with name: " + name + " already exists");
        if (route.getOwner() == null) {
            User owner = userService.findByUsername(principal);
            route.setOwner(owner);
        }
        return routeRepository.save(buildRoute(route));
    }

    public  Route mapToRoute(RouteDto routeDto){
        Route route = new Route();
        Terminal start = terminalService.findById(routeDto.getStartTerminalId());
        Terminal stop = terminalService.findById(routeDto.getStopTerminalId());
        route.setName(start + " - " + stop);
        route.setStartTerminal(start);
        route.setStopTerminal(stop);
        route.setFare(routeDto.getFare());
        route.setMode(convertToTransportModeEnum(routeDto.getTransportMode()));
        return route;
    }

    public Route findById(long id) {
        return routeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route does not exist"));
    }

    public Route findById(long id, String principal) {
        return routeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route does not exist"));
    }

    public List<Route> findByMode(Enum.TransportMode type) {
        return routeRepository.findAllByMode(type);
    }

    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Route update(Route route, String principal) {
        Optional<Route> existing = routeRepository.findById(route.getId());
        if (existing.isPresent()) {
            String startTerminalName = route.getStartTerminal().getName();
            String stopTerminalName = route.getStopTerminal().getName();
            route.setName(startTerminalName + " - " + stopTerminalName);
            if (route.getOwner() == null) {
                User owner = userService.findByUsername(principal);
                route.setOwner(owner);
            }
            return routeRepository.save(buildRoute(route));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route does not exist");
    }

    public void delete(long id, String principal) {

        log.info("wanna delete route with id===>"+id);
        Optional<Route> existing = routeRepository.findById(id);
        if (existing.isPresent()) {
            routeRepository.deleteById(id);
        }
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

    private Route buildRoute(Route route) {
        Terminal startTerminal = route.getStartTerminal();
        if (startTerminal != null)
            route.setStartTerminal(terminalService.findById(startTerminal.getId()));

        Terminal stopTerminal = route.getStopTerminal();
        if (stopTerminal != null)
            route.setStopTerminal(terminalService.findById(stopTerminal.getId()));
        return route;
    }

    //BUS, CAR, RAIL, FERRY, RICKSHAW
    private Enum.TransportMode convertToTransportModeEnum(String name){

        if(name.startsWith("BUS") || name.equalsIgnoreCase("bus")){
            return Enum.TransportMode.BUS;
        }

        if(name.startsWith("CAR") || name.equalsIgnoreCase("car")){
            return Enum.TransportMode.CAR;
        }

        if(name.startsWith("RAIL") || name.equalsIgnoreCase("rail")){
            return Enum.TransportMode.RAIL;
        }

        if(name.startsWith("FERRY") || name.equalsIgnoreCase("ferry")){
            return Enum.TransportMode.FERRY;
        }

        return Enum.TransportMode.RICKSHAW;
    }
}
