package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Page;
import com.interswitch.smartmoveserver.model.Schedule;
import com.interswitch.smartmoveserver.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleApi {

    @Autowired
    ScheduleService scheduleService;

    @GetMapping(produces = "application/json")
    private Page<Schedule> findAll(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size, Principal principal) {
        org.springframework.data.domain.Page pageable = scheduleService.findAllPaginated(principal, page, size);
        return new Page<Schedule>(pageable.getTotalElements(), pageable.getContent());    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Schedule findById(@Validated @PathVariable long id, Principal principal) {
        return scheduleService.findById(id, principal);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Schedule save(@Validated @RequestBody Schedule schedule, Principal principal) {
        return scheduleService.save(schedule, principal);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Schedule update(@Validated @RequestBody Schedule schedule, Principal principal) {
        return scheduleService.update(schedule, principal);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void delete(@Validated @PathVariable long id, Principal principal) {
        scheduleService.delete(id, principal);
    }
}
