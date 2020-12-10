package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.FeeConfiguration;
import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.Trip;
import com.interswitch.smartmoveserver.service.FeeConfigurationService;
import com.interswitch.smartmoveserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fees")
public class FeeConfigurationApi {

    @Autowired
    private FeeConfigurationService feeConfigurationService;

    @GetMapping(produces = "application/json")
    private PageView<FeeConfiguration> findAll(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return feeConfigurationService.findAllPaginated(0L,page, size, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private FeeConfiguration findById(@Validated @PathVariable long id) {
        return feeConfigurationService.findById(id,JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private FeeConfiguration save(@Validated @RequestBody FeeConfiguration feeConfiguration) {
        return feeConfigurationService.save(feeConfiguration,JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

/*    @GetMapping(value = "/vehicle/{regNo}", produces = "application/json")
    private List<Trip> findByVehicleRegNo(@Validated @PathVariable String regNo) {
        return tripService.findByVehicleRegNo(regNo);
    }*/

    @PutMapping(produces = "application/json", consumes = "application/json")
    private FeeConfiguration update(@Validated @RequestBody FeeConfiguration feeConfiguration) {
        return feeConfigurationService.update(feeConfiguration,JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void delete(@Validated @PathVariable long id) {
        feeConfigurationService.delete(id,JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }
}
