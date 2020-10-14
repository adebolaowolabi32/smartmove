package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.repository.TripRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * @author adebola.owolabi
 */
@Service
@Transactional
public class TripService {

    @Autowired
    TripRepository tripRepository;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    UserService userService;

    @Autowired
    PageUtil pageUtil;

    public List<Trip> findAll() {
        return tripRepository.findAll();
    }

    public Page<Trip> findAllPaginated(Principal principal, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        return tripRepository.findAll(pageable);

    }

    public Trip save(Trip trip, Principal principal) {
        long id = trip.getId();
        boolean exists = tripRepository.existsById(id);

        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Trip already exists");
        trip.setReferenceNo(RandomUtil.getRandomNumber(6));
        if(trip.getOwner() == null) {
            User owner = userService.findByUsername(principal.getName());
            trip.setOwner(owner);
        }
        return tripRepository.save(buildVehicle(trip));
    }

    public Trip findById(long id) {
        return tripRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip does not exist"));
    }

    public Trip findById(long id, Principal principal) {
        return tripRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip does not exist"));
    }

    public Trip update(Trip trip, Principal principal) {
        Optional<Trip> existing = tripRepository.findById(trip.getId());
        if (existing.isPresent())
        {
            if(trip.getOwner() == null) {
                User owner = userService.findByUsername(principal.getName());
                trip.setOwner(owner);
            }
            return tripRepository.save(buildVehicle(trip));
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip does not exist");
    }

    public void delete(long id, Principal principal) {
        Optional<Trip> existing = tripRepository.findById(id);
        if (existing.isPresent())
            tripRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip does not exist");
        }
    }

    //findByVehicleRegNo, findByDriverName

    public Long countAll() {
        return tripRepository.count();
    }

    private Trip buildVehicle(Trip trip) {
        Vehicle vehicle = trip.getVehicle();
        if(vehicle != null)
            trip.setVehicle(vehicleService.findById(vehicle.getId()));

        Schedule schedule = trip.getSchedule();
        if(schedule != null)
            trip.setSchedule(scheduleService.findById(schedule.getId()));

        User driver = trip.getDriver();
        if(driver != null)
            trip.setDriver(userService.findById(driver.getId()));
        return trip;
    }
}
