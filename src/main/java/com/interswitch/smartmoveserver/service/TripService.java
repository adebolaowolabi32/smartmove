package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.dto.TripDto;
import com.interswitch.smartmoveserver.repository.TripRepository;
import com.interswitch.smartmoveserver.util.FileParser;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.RandomUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
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

    private static String PREFIX_SEPARATOR = "|";
    @Autowired
    VehicleService vehicleService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    UserService userService;
    @Autowired
    TripReferenceService tripReferenceService;

    @Autowired
    PageUtil pageUtil;
    @Autowired
    SecurityUtil securityUtil;

    public List<Trip> findAll() {
        return tripRepository.findAll();
    }

    public List<Trip> findAll(Long owner, String principal) {
        User user = userService.findByUsername(principal);
        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                return tripRepository.findAllByOwner(user);
            } else {
                return tripRepository.findAll();
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                return tripRepository.findAllByOwner(ownerUser);
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public PageView<Trip> findAllPaginated(Long owner, int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        User user = userService.findByUsername(principal);
        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                Page<Trip> pages = tripRepository.findAllByOwner(pageable, user);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            } else {
                Page<Trip> pages = tripRepository.findAll(pageable);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                Page<Trip> pages = tripRepository.findAllByOwner(pageable, ownerUser);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Trip save(Trip trip) {
        return tripRepository.save(trip);
    }

    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Trip save(Trip trip, String principal) {
        TripReference tripReference = tripReferenceService.findByOwner(principal);
        String prefix = "";
        if (tripReference != null && tripReference.isEnabled())
            prefix = tripReference.getPrefix() + PREFIX_SEPARATOR;
        trip.setReferenceNo(prefix + RandomUtil.getRandomNumber(6));
        if (trip.getOwner() == null) {
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

    public List<Trip> findByOwner(User user) {
        return tripRepository.findAllByOwner(user);
    }


    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Trip update(Trip trip) {
        Optional<Trip> existing = tripRepository.findById(trip.getId());
        if (existing.isPresent()) {
            return tripRepository.save(trip);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip does not exist");
    }


    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Trip update(Trip trip, String principal) {
        Optional<Trip> existing = tripRepository.findById(trip.getId());
        if (existing.isPresent()) {
            if (trip.getOwner() == null) {
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

    public Long countByOwner(String username) {
        User user = userService.findByUsername(username);
        return tripRepository.countByOwner(user);
    }

    public Long countAll() {
        return tripRepository.count();
    }

    private Trip buildVehicle(Trip trip) {
        Vehicle vehicle = trip.getVehicle();
        if (vehicle != null)
            trip.setVehicle(vehicleService.findById(vehicle.getId()));

        Schedule schedule = trip.getSchedule();
        if (schedule != null)
            trip.setSchedule(scheduleService.findById(schedule.getId()));

        User driver = trip.getDriver();
        if (driver != null)
            trip.setDriver(userService.findById(driver.getId()));
        return trip;
    }

    public boolean upload(MultipartFile file, String principal) throws IOException {
        User owner = userService.findByUsername(principal);
        List<Trip> savedTrips = new ArrayList<>();
        if (file.getSize() > 1) {
            FileParser<TripDto> fileParser = new FileParser<>();
            List<TripDto> tripDtoList = fileParser.parseFileToEntity(file, TripDto.class);
            tripDtoList.forEach(tripDto -> {
                savedTrips.add(tripRepository.save(mapToTrip(tripDto, owner)));

            });
        }
        return savedTrips.size() > 1;
    }

    private Trip mapToTrip(TripDto tripDto, User owner) {
        User driver = userService.findByEmail(tripDto.getDriverEmail());
        Schedule schedule = scheduleService.findById(tripDto.getScheduleId());
        Vehicle vehicle = vehicleService.findByRegNo(tripDto.getVehicleNumber());
        return Trip.builder()
                .driver(driver)
                .schedule(schedule)
                .referenceNo(RandomUtil.getRandomNumber(6))
                .vehicle(vehicle)
                .owner(owner)
                .build();
    }
}