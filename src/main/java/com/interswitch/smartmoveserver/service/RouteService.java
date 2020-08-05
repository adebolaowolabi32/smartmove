package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
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
import java.util.Set;

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

    public List<Route> getAll() {
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
            Optional<User> owner = userRepository.findByUsername(principal.getName());
            if (owner.isPresent()) route.setOwner(owner.get());
        }
        return routeRepository.save(route);
    }

    public Route findById(long id) {
        return routeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route does not exist"));
    }

    public List<Route> find(Enum.TransportMode type) {
        return routeRepository.findAllByType(type);
    }

    public List<Route> findByOwner(long owner) {
        Optional<User> user = userRepository.findById(owner);
        if (user.isPresent())
            return routeRepository.findAllByOwner(user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }

    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    public Route update(Route route) {
        Optional<Route> existing = routeRepository.findById(route.getId());
        if (existing.isPresent()) {
            Terminal startTerminal = terminalService.findById(route.getStartTerminalId());
            Terminal stopTerminal = terminalService.findById(route.getStopTerminalId());
            String startTerminalName = startTerminal.getName();
            String stopTerminalName = stopTerminal.getName();
            route.setStartTerminalName(startTerminalName);
            route.setStopTerminalName(stopTerminalName);
            route.setName(startTerminalName + " - " + stopTerminalName);
            return routeRepository.save(route);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route does not exist");
    }

    public void assignToVehicle(long routeId, long vehicleId) {
        Optional<Route> routeOptional = routeRepository.findById(routeId);
        if (routeOptional.isPresent()) {
            Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
            if (vehicleOptional.isPresent()) {
                Vehicle vehicle = vehicleOptional.get();
                Route route = routeOptional.get();
                Set<Vehicle> vehicles = route.getVehicles();
                vehicles.add(vehicle);
                route.setVehicles(vehicles);
                routeRepository.save(route);
                Set<Route> routes = vehicle.getRoutes();
                routes.add(route);
                vehicle.setRoutes(routes);
                vehicleRepository.save(vehicle);
            } else throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Vehicle does not exist");
        } else throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Route does not exist");
    }

    public void unassignFromVehicle(long routeId, long vehicleId) {
        Optional<Route> route = routeRepository.findById(routeId);
        if (route.isPresent()) {
            Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
            if (vehicle.isPresent()) {
                Set<Route> routes = vehicle.get().getRoutes();
                routes.remove(route.get());
            } else throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Vehicle does not exist");
        } else throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Route does not exist");
    }

    public void delete(long id) {
        Optional<Route> existing = routeRepository.findById(id);
        if (existing.isPresent())
            routeRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route does not exist");
        }
    }

    public void activate(long routeId) {
        Optional<Route> routeOptional = routeRepository.findById(routeId);
        if (routeOptional.isPresent()) {
            Route route = routeOptional.get();
            route.setEnabled(true);
            routeRepository.save(route);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route does not exist");
    }

    public void deactivate(long routeId) {
        Optional<Route> routeOptional = routeRepository.findById(routeId);
        if (routeOptional.isPresent()) {
            Route route = routeOptional.get();
            route.setEnabled(false);
            routeRepository.save(route);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route does not exist");
    }

    public Long countByOwner(User user) {
        return routeRepository.countByOwner(user);
    }

    public Long countAll() {
        return routeRepository.count();
    }
}
