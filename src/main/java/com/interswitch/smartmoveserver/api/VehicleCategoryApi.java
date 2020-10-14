package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Page;
import com.interswitch.smartmoveserver.model.VehicleCategory;
import com.interswitch.smartmoveserver.service.VehicleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/vehicleCategories")
public class VehicleCategoryApi {
    @Autowired
    VehicleCategoryService vehicleCategoryService;

    @GetMapping(produces = "application/json")
    private Page<VehicleCategory> findAll(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size, Principal principal) {
        org.springframework.data.domain.Page pageable = vehicleCategoryService.findAllPaginated(principal, 0L, page, size);
        return new Page<VehicleCategory>(pageable.getTotalElements(), pageable.getContent());    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private VehicleCategory save(@Validated @RequestBody VehicleCategory vehicleCategory, Principal principal) {
        return vehicleCategoryService.save(vehicleCategory, principal);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private VehicleCategory findById(@Validated @PathVariable long id, Principal principal) {
        return vehicleCategoryService.findById(id, principal);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private VehicleCategory update(@Validated @RequestBody VehicleCategory vehicleCategory, Principal principal) {
        return vehicleCategoryService.update(vehicleCategory, principal);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id, Principal principal) {
        vehicleCategoryService.delete(id, principal);
    }
}
