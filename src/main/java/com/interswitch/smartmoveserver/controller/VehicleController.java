package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @GetMapping(produces = "application/json")
    private Iterable<Vehicle> getAll() {

        return vehicleService.getAll();
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Vehicle save(@Validated @RequestBody Vehicle vehicle) {
        return vehicleService.save(vehicle);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Vehicle findById(@Validated @PathVariable long id) {
        return vehicleService.findById(id);
    }

    @GetMapping(value = "/findByOwner/{owner}", produces = "application/json")
    private Iterable<Vehicle> getByOwner(@Validated @PathVariable long owner) {
        return vehicleService.findAllByOwner(owner);
    }
    @GetMapping(value = "/findByType/{type}", produces = "application/json")
    private Iterable<Vehicle> find(@Validated @PathVariable Enum.TransportMode type) {
        return vehicleService.find(type);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Vehicle update(@Validated @RequestBody Vehicle vehicle) {
        return vehicleService.update(vehicle);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        vehicleService.delete(id);
    }

    @PostMapping(value = "/activate/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void activate(@Validated @PathVariable long id) {
        vehicleService.activate(id);
    }

    @PostMapping(value = "/deactivate/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void deactivate(@Validated @PathVariable long id) {
        vehicleService.deactivate(id);
    }
}
