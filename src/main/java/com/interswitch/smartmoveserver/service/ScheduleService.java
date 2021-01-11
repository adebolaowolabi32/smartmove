package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.repository.ScheduleRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
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
    RouteService routeService;

    @Autowired
    VehicleCategoryService vehicleCategoryService;

    @Autowired
    UserService userService;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findAll(Long owner, String principal) {
        User user = userService.findByUsername(principal);
        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                return scheduleRepository.findAllByOwner(user);
            } else {
                return scheduleRepository.findAll();
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                return scheduleRepository.findAllByOwner(ownerUser);
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public PageView<Schedule> findAllPaginated(Long owner, int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        User user = userService.findByUsername(principal);
        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                Page<Schedule> pages = scheduleRepository.findAllByOwner(pageable, user);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            } else {
                Page<Schedule> pages = scheduleRepository.findAll(pageable);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                Page<Schedule> pages = scheduleRepository.findAllByOwner(pageable, ownerUser);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }


    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Schedule save(Schedule schedule, String principal) {
        LocalDateTime start = LocalDateTime.of(schedule.getDepartureDate(), schedule.getDepartureTime());
        LocalDateTime stop = LocalDateTime.of(schedule.getArrivalDate(), schedule.getArrivalTime());
        Duration duration = Duration.between(start, stop);
        if (duration.isNegative())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Departure date must not exceed arrival date.");
        schedule.setDuration(String.valueOf(duration.getSeconds() / 60 / 60));
        if (schedule.getOwner() == null) {
            User owner = userService.findByUsername(principal);
            schedule.setOwner(owner);
        }
        return scheduleRepository.save(buildSchedule(schedule));
    }

    public Schedule findById(long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist"));
    }

    public Schedule findById(long id, String principal) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist"));
    }

    public List<Schedule> findByOwner(String username) {
        User owner = userService.findByUsername(username);
        if (owner.getRole() == Enum.Role.ISW_ADMIN)
            return scheduleRepository.findAll();
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

        Route route = schedule.getRoute();
        if (route != null)
            schedule.setRoute(routeService.findById(route.getId()));
        return schedule;
    }
}
