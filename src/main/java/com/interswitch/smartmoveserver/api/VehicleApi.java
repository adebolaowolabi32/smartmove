package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/vehicles")
public class VehicleApi {
    @Autowired
    VehicleService vehicleService;

    @GetMapping(produces = "application/json")
    private List<Vehicle> findAll() {
        return vehicleService.findAll();
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
    private List<Vehicle> getByOwner(@Validated @PathVariable long owner) {
        return vehicleService.findAllByOwner(owner);
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
