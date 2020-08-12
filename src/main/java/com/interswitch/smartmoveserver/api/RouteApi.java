package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/routes")
public class RouteApi {
    @Autowired
    RouteService routeService;

    @GetMapping(produces = "application/json")
    private List<Route> findAll() {

        return routeService.findAll();
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Route save(@Validated @RequestBody Route route) {
        return routeService.save(route);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Route findById(@Validated @PathVariable long id) {
        return routeService.findById(id);
    }

    @GetMapping(value = "/findByType/{type}", produces = "application/json")
    private List<Route> find(@Validated @PathVariable Enum.TransportMode type) {
        return routeService.find(type);
    }

    @GetMapping(value = "/findByOwner/{owner}", produces = "application/json")
    private List<Route> findByOwner(@Validated @PathVariable long owner) {
        return routeService.findByOwner(owner);
    }

   /* @PostMapping(value = "/{routeId}/assignToVehicle/{vehicleId}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void assignToVehicle(@Validated @PathVariable long routeId, @Validated @PathVariable long vehicleId) {
        routeService.assignToVehicle(routeId, vehicleId);
    }

    @DeleteMapping(value = "/{routeId}/unAssignFromVehicle/{vehicleId}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void unassignFromVehicle(@Validated @PathVariable long routeId, @Validated @PathVariable long vehicleId) {
        routeService.unassignFromVehicle(routeId, vehicleId);
    }*/

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Route update(@Validated @RequestBody Route route) {
        return routeService.update(route);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        routeService.delete(id);
    }

    @PostMapping(value = "/activate/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void activate(@Validated @PathVariable long id) {
        routeService.activate(id);
    }

    @PostMapping(value = "/deactivate/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void deactivate(@Validated @PathVariable long id) {
        routeService.deactivate(id);
    }
}
