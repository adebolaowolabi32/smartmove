package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.repository.ScheduleRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
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
    UserService userService;

    @Autowired
    PageUtil pageUtil;

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public PageView<Schedule> findAllPaginated(int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Page<Schedule> pages = scheduleRepository.findAll(pageable);
        return new PageView<>(pages.getTotalElements(), pages.getContent());
    }

    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Schedule save(Schedule schedule, String principal) {
        LocalDateTime start = LocalDateTime.of(schedule.getDepartureDate(), schedule.getDepartureTime());
        LocalDateTime stop = LocalDateTime.of(schedule.getArrivalDate(), schedule.getArrivalTime());
        Duration duration = Duration.between(start, stop);
        schedule.setDuration(String.valueOf(duration.getSeconds() / 60 / 60));
        if (schedule.getOwner() == null) {
            User owner = userService.findByUsername(principal);
            schedule.setOwner(owner);
        }
        return scheduleRepository.save(buildSchedule(schedule));
    }

    public Schedule findById(long id) {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        return schedule.get();
    }

    public Schedule findById(long id, String principal) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist"));
    }

    public List<Schedule> findByOwner(String username) {
        User owner = userService.findByUsername(username);
        return scheduleRepository.findAllByOwner(owner);
    }


    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Schedule update(Schedule schedule, String principal) {
        Optional<Schedule> existing = scheduleRepository.findById(schedule.getId());
        if (existing.isPresent()){
            if (schedule.getOwner() == null) {
                User owner = userService.findByUsername(principal);
                schedule.setOwner(owner);
            }
            return scheduleRepository.save(buildSchedule(schedule));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist");
    }

    public void delete(long id, String principal) {
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
