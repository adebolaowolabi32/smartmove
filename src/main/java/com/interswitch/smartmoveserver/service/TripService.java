package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.dto.TripDto;
import com.interswitch.smartmoveserver.repository.ScheduleRepository;
import com.interswitch.smartmoveserver.repository.TripRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.VehicleRepository;
import com.interswitch.smartmoveserver.util.FileParser;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
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
    UserRepository userRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PageUtil pageUtil;

    public List<Trip> findAll() {
        return tripRepository.findAll();
    }

    public PageView<Trip> findAllPaginated(int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Page<Trip> pages = tripRepository.findAll(pageable);
        return new PageView<>(pages.getTotalElements(), pages.getContent());
    }

    public Trip save(Trip trip) {
        return tripRepository.save(trip);
    }

    public Trip save(Trip trip, String principal) {
        trip.setReferenceNo(RandomUtil.getRandomNumber(6));
        if(trip.getOwner() == null) {
            User owner = userService.findByUsername(principal);
            trip.setOwner(owner);
        }
        return tripRepository.save(buildVehicle(trip));
    }

    public Trip findById(long id) {
        return tripRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip does not exist"));
    }

    public Trip findById(long id, String principal) {
        return tripRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip does not exist"));
    }

    public Trip update(Trip trip) {
        Optional<Trip> existing = tripRepository.findById(trip.getId());
        if (existing.isPresent())
        {
            return tripRepository.save(trip);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip does not exist");
    }

    public Trip update(Trip trip, String principal) {
        Optional<Trip> existing = tripRepository.findById(trip.getId());
        if (existing.isPresent())
        {
            if(trip.getOwner() == null) {
                User owner = userService.findByUsername(principal);
                trip.setOwner(owner);
            }
            return tripRepository.save(buildVehicle(trip));
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip does not exist");
    }

    public void delete(long id, String principal) {
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
    public boolean upload(MultipartFile file, String principal) throws IOException {
        User owner = userService.findByUsername(principal);
        List<Trip> savedTrips = new ArrayList<>();
        if(file.getSize()>1){
            FileParser<TripDto> fileParser = new FileParser<>();
            List<TripDto> tripDtoList = fileParser.parseFileToEntity(file, TripDto.class);
            tripDtoList.forEach(tripDto->{
                savedTrips.add( tripRepository.save(mapToTrip(tripDto, owner)));

            });
        }
        return savedTrips.size()>1;
    }

    private Trip mapToTrip(TripDto tripDto, User owner){

        Optional<User> driverOptional = userRepository.findByEmail(tripDto.getDriverEmail());
        User driver = driverOptional.isPresent() ? driverOptional.get() : null;

        Optional<Schedule> scheduleOptional = scheduleRepository.findById(tripDto.getScheduleId());
        Schedule schedule = scheduleOptional.isPresent() ? scheduleOptional.get() : null;

        Vehicle vehicle = vehicleRepository.findByRegNo(tripDto.getVehicleNumber());

        return Trip.builder()
                .driver(driver)
                .schedule(schedule)
                .mode(convertToModeOfTransportEnum(tripDto.getTransportMode()))
                .referenceNo(RandomUtil.getRandomNumber(6))
                .vehicle(vehicle)
                .owner(owner)
                .build();
    }

    private Enum.TransportMode convertToModeOfTransportEnum(String mode){
        // KEKE, BUS, CAR, RAIL, FERRY, RICKSHAW
        return Enum.TransportMode.valueOf(mode.toUpperCase());
    }
}
