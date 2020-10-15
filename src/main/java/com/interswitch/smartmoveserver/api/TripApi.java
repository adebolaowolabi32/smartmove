package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Page;
import com.interswitch.smartmoveserver.model.Trip;
import com.interswitch.smartmoveserver.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/trips")
public class TripApi {

    @Autowired
    TripService tripService;

    @GetMapping(produces = "application/json")
    private Page<Trip> findAll(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size, Principal principal) {
        org.springframework.data.domain.Page pageable = tripService.findAllPaginated(principal, page, size);
        return new Page<Trip>(pageable.getTotalElements(), pageable.getContent());    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Trip findById(@Validated @PathVariable long id, Principal principal) {
        return tripService.findById(id, principal);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Trip save(@Validated @RequestBody Trip trip, Principal principal) {
        return tripService.save(trip, principal);
    }

/*    @GetMapping(value = "/vehicle/{regNo}", produces = "application/json")
    private List<Trip> findByVehicleRegNo(@Validated @PathVariable String regNo) {
        return tripService.findByVehicleRegNo(regNo);
    }*/

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Trip update(@Validated @RequestBody Trip trip, Principal principal) {
        return tripService.update(trip, principal);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void delete(@Validated @PathVariable long id, Principal principal) {
        tripService.delete(id, principal);
    }
}
