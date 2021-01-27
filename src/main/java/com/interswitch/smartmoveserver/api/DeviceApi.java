package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.service.DeviceService;
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
@RequestMapping("/api/devices")
public class DeviceApi {

    @Autowired
    private DeviceService deviceService;

    @GetMapping(produces = "application/json")
    private PageView<Device> findAll(@RequestParam(required = false, defaultValue = "0") Long owner, @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return deviceService.findAllPaginated(owner, page, size, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Device save(@Validated @RequestBody Device device) {
        return deviceService.save(device, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Device findById(@Validated @PathVariable long id) {
        return deviceService.findById(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Device update(@Validated @RequestBody Device device) {
        return deviceService.update(device, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        deviceService.delete(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }
}
