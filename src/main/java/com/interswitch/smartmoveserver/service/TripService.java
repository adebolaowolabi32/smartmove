package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.dto.ManifestDto;
import com.interswitch.smartmoveserver.model.dto.TripDto;
import com.interswitch.smartmoveserver.repository.ScheduleRepository;
import com.interswitch.smartmoveserver.repository.TripRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.VehicleRepository;
import com.interswitch.smartmoveserver.util.FileParser;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.RandomUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * Trips management service
 */
@Service
@Transactional
public class TripService {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    TripRepository tripRepository;

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

    public Page<Trip> findAllPaginated(int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        return tripRepository.findAll(pageable);

    }

    public Trip save(Trip trip) {
        long id = trip.getId();
        boolean exists = tripRepository.existsById(id);

        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Trip already exists");

        trip.setReferenceNo(RandomUtil.getRandomNumber(6));
        return tripRepository.save(trip);
    }

    public Trip findById(long id) {
        return tripRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip does not exist"));
    }

    public Trip update(Trip trip) {
        Optional<Trip> existing = tripRepository.findById(trip.getId());
        if (existing.isPresent())
            return tripRepository.save(trip);

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip does not exist");
    }

    public void delete(long id) {
        Optional<Trip> existing = tripRepository.findById(id);
        if (existing.isPresent())
            tripRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip does not exist");
        }
    }

    public List<Trip> findByDriverUsername(String username) {
        return tripRepository.findByDriverUsername(username);
    }


/*    public List<Trip> findByVehicleRegNo(String vehicleRegNo) {
        return tripRepository.findByVehicleRegNo(vehicleRegNo);
    }*/

    public Long countAll() {
        return tripRepository.count();
    }

    public boolean upload(MultipartFile file) throws IOException {

        List<Trip> savedTrips = new ArrayList<>();
        if(file.getSize()>1){
            logger.info("upload trips===>");
            FileParser<TripDto> fileParser = new FileParser<>();
            List<TripDto> tripDtoList = fileParser.parseFileToEntity(file, TripDto.class);
            tripDtoList.forEach(tripDto->{
                logger.info("upload trips===>"+tripDto);
                savedTrips.add( tripRepository.save(mapToTrip(tripDto)));

            });
        }
        return savedTrips.size()>1;
    }

    private Trip mapToTrip(TripDto tripDto){

        Optional<User> driverOptional = userRepository.findByEmail(tripDto.getDriverEmail());
        User driver = driverOptional.isPresent() ? driverOptional.get() : null;

        Optional<Schedule> scheduleOptional = scheduleRepository.findById(tripDto.getScheduleId());
        Schedule schedule = scheduleOptional.isPresent() ? scheduleOptional.get() : null;

        Vehicle vehicle = vehicleRepository.findByRegNo(tripDto.getVehicleNumber());

        return Trip.builder()
                .driver(driver)
                .schedule(schedule)
                .mode(convertToModeOfTransportEnum(tripDto.getTransportMode()))
                .vehicle(vehicle)
                .build();
    }

    private Enum.TransportMode convertToModeOfTransportEnum(String mode){
        // KEKE, BUS, CAR, RAIL, FERRY, RICKSHAW
        return Enum.TransportMode.valueOf(mode.toUpperCase());
    }
}
