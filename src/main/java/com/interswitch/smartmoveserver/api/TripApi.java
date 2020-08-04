package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Trip;
import com.interswitch.smartmoveserver.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripApi {

    @Autowired
    TripService tripService;

    @GetMapping(produces = "application/json")
    private List<Trip> getAll() {
        return tripService.getAll();
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Trip save(@Validated @RequestBody Trip trip) {
        return tripService.save(trip);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Trip findById(@Validated @PathVariable long id) {
        return tripService.findById(id);
    }

    @GetMapping(value = "/route/{id}", produces = "application/json")
    private List<Trip> findByRoute(@Validated @PathVariable long id) {
        return tripService.findByRouteId(id);
    }


    @GetMapping(value = "/vehicle/{regNo}", produces = "application/json")
    private List<Trip> findByVehicleRegNo(@Validated @PathVariable String regNo) {
        return tripService.findByVehicleRegNo(regNo);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void delete(@Validated @PathVariable long id) {
        tripService.delete(id);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Trip update(@Validated @RequestBody Trip trip) {
        return tripService.update(trip);
    }

}
