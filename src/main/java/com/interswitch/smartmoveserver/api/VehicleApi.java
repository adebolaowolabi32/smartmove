package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.service.VehicleService;
import com.interswitch.smartmoveserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/vehicles")
public class VehicleApi {
    @Autowired
    VehicleService vehicleService;

    @GetMapping(produces = "application/json")
    private PageView<Vehicle> findAll(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        return vehicleService.findAllPaginated(0L, page, size, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Vehicle save(@Validated @RequestBody Vehicle vehicle) {
        return vehicleService.save(vehicle,JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Vehicle findById(@Validated @PathVariable long id) {
        return vehicleService.findById(id,JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Vehicle update(@Validated @RequestBody Vehicle vehicle) {
        return vehicleService.update(vehicle,JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        vehicleService.delete(id,JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }
}
