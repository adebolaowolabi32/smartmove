package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.PageView;
import com.interswitch.smartmoveserver.model.Schedule;
import com.interswitch.smartmoveserver.service.ScheduleService;
import com.interswitch.smartmoveserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleApi {

    @Autowired
    ScheduleService scheduleService;

    @GetMapping(produces = "application/json")
    private PageView<Schedule> findAll(@RequestParam(required = false, defaultValue = "0") Long owner, @RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return scheduleService.findAllPaginated(owner, page, size, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Schedule findById(@Validated @PathVariable long id) {
        return scheduleService.findById(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Schedule save(@Validated @RequestBody Schedule schedule) {
        return scheduleService.save(schedule, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Schedule update(@Validated @RequestBody Schedule schedule) {
        return scheduleService.update(schedule, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    private void delete(@Validated @PathVariable long id) {
        scheduleService.delete(id, JwtUtil.getUsername(SecurityContextHolder.getContext().getAuthentication()));
    }
}