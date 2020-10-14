package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Page;
import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/vehicles")
public class VehicleApi {
    @Autowired
    VehicleService vehicleService;

    @GetMapping(produces = "application/json")
    private Page<Vehicle> findAll(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size, Principal principal) {
        org.springframework.data.domain.Page pageable = vehicleService.findAllPaginated(principal, 0L, page, size);
        return new Page<Vehicle>(pageable.getTotalElements(), pageable.getContent());
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Vehicle save(@Validated @RequestBody Vehicle vehicle, Principal principal) {
        return vehicleService.save(vehicle, principal);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Vehicle findById(@Validated @PathVariable long id, Principal principal) {
        return vehicleService.findById(id, principal);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Vehicle update(@Validated @RequestBody Vehicle vehicle, Principal principal) {
        return vehicleService.update(vehicle, principal);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id, Principal principal) {
        vehicleService.delete(id, principal);
    }
}
