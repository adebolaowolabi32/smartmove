package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.Trip;
import com.interswitch.smartmoveserver.service.TripService;
import com.interswitch.smartmoveserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
public class TripApi {

    @Autowired
    TripService tripService;

    @GetMapping(produces = "application/json")
    private PageView<Trip> findAll(@RequestParam(required = false, defaultValue = "0") Long owner, @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        return tripService.findAllPaginated(owner, page, size, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Trip findById(@Validated @PathVariable long id) {
        return tripService.findById(id,JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Trip save(@Validated @RequestBody Trip trip) {
        return tripService.save(trip,JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

/*    @GetMapping(value = "/vehicle/{regNo}", produces = "application/json")
    private List<Trip> findByVehicleRegNo(@Validated @PathVariable String regNo) {
        return tripService.findByVehicleRegNo(regNo);
    }*/

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Trip update(@Validated @RequestBody Trip trip) {
        return tripService.update(trip,JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void delete(@Validated @PathVariable long id) {
        tripService.delete(id,JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }
}