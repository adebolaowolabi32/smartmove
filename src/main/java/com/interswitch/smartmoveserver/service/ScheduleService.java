package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.repository.ScheduleRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

/*
 * Created by adebola.owolabi on 8/7/2020
 */
@Service
@Transactional
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    TerminalService terminalService;

    @Autowired
    VehicleCategoryService vehicleCategoryService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PageUtil pageUtil;

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public Page<Schedule> findAllPaginated(Principal principal, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        return scheduleRepository.findAll(pageable);
    }

    public Schedule save(Schedule schedule, Principal principal) {
        long id = schedule.getId();
        boolean exists = scheduleRepository.existsById(id);

        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Schedule already exists");

        Duration duration = Duration.between(schedule.getDepartureTime(), schedule.getArrivalTime());
        schedule.setDuration(String.valueOf(duration.getSeconds() / 60 / 60));
        /*schedule.setArrivalDateString(DateUtil.formatDate(schedule.getArrivalDate()));
        schedule.setDepartureDateString(DateUtil.formatDate(schedule.getDepartureDate()));
        schedule.setArrivalTimeString(DateUtil.formatTime(schedule.getArrivalTime()));
        schedule.setDepartureTimeString(DateUtil.formatTime(schedule.getDepartureTime()));*/
        //schedule.setName(schedule.getStartTerminal().getName() + " - " + schedule.getStopTerminal().getName() + " " + schedule.getDepartureString());
        if (schedule.getOwner() == null) {
            User owner = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));
            schedule.setOwner(owner);
        }
        return scheduleRepository.save(buildSchedule(schedule));
    }

    public Schedule findById(long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist"));
    }

    public Schedule findById(long id, Principal principal) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist"));
    }

    public Schedule update(Schedule schedule, Principal principal) {
        Optional<Schedule> existing = scheduleRepository.findById(schedule.getId());
        if (existing.isPresent()){
            if (schedule.getOwner() == null) {
                User owner = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));
                schedule.setOwner(owner);
            }
            return scheduleRepository.save(buildSchedule(schedule));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist");
    }

    public void delete(long id, Principal principal) {
        Optional<Schedule> existing = scheduleRepository.findById(id);
        if (existing.isPresent())
            scheduleRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist");
        }
    }

    private Schedule buildSchedule(Schedule schedule) {
        VehicleCategory vehicleCategory = schedule.getVehicle();
        if(vehicleCategory != null)
            schedule.setVehicle(vehicleCategoryService.findById(vehicleCategory.getId()));

        Terminal startTerminal = schedule.getStartTerminal();
        if(startTerminal != null)
            schedule.setStartTerminal(terminalService.findById(startTerminal.getId()));

        Terminal stopTerminal = schedule.getStopTerminal();
        if(stopTerminal != null)
            schedule.setStopTerminal(terminalService.findById(stopTerminal.getId()));
        return schedule;
    }
}
