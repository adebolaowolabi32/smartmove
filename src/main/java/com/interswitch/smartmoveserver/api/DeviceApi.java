package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.Page;
import com.interswitch.smartmoveserver.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/api/devices")
public class DeviceApi {

    @Autowired
    private DeviceService deviceService;

    @GetMapping(produces = "application/json")
    private Page<Device> findAll(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size, Principal principal) {
        org.springframework.data.domain.Page pageable = deviceService.findAllPaginated(principal, 0L, page, size);
        return new Page<Device>(pageable.getTotalElements(), pageable.getContent());
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Device save(@Validated @RequestBody Device device, Principal principal) {
        return deviceService.save(device, principal);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Device findById(@Validated @PathVariable long id, Principal principal) {
        return deviceService.findById(id, principal);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Device update(@Validated @RequestBody Device device, Principal principal) {
        return deviceService.update(device, principal);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id, Principal principal) {
        deviceService.delete(id, principal);
    }
}
