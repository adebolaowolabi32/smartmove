package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Manifest;
import com.interswitch.smartmoveserver.model.Schedule;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.view.Shuffle;
import com.interswitch.smartmoveserver.repository.ScheduleRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ManifestService manifestService;

    @Autowired
    PageUtil pageUtil;

    public List<Schedule> shuffle(Shuffle shuffle) {
        Manifest manifest = manifestService.findById(Long.parseLong(shuffle.getManifestId()));
        manifest.setSchedule(shuffle.getSchedule());
        manifestService.update(manifest);
        return scheduleRepository.findAll();
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findByOwner(User operator) {
        return scheduleRepository.findByOwner(operator);
    }
    public Page<Schedule> findAllPaginated(int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        return scheduleRepository.findAll(pageable);
    }

    public Schedule save(Schedule schedule) {
        long id = schedule.getId();
        boolean exists = scheduleRepository.existsById(id);

        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Schedule already exists");

        LocalDateTime start = LocalDateTime.of(schedule.getDepartureDate(), schedule.getDepartureTime());
        LocalDateTime stop = LocalDateTime.of(schedule.getArrivalDate(), schedule.getArrivalTime());
        Duration duration = Duration.between(start, stop);
        schedule.setDuration(String.valueOf(duration.getSeconds() / 60 / 60));
        /*schedule.setArrivalDateString(DateUtil.formatDate(schedule.getArrivalDate()));
        schedule.setDepartureDateString(DateUtil.formatDate(schedule.getDepartureDate()));
        schedule.setArrivalTimeString(DateUtil.formatTime(schedule.getArrivalTime()));
        schedule.setDepartureTimeString(DateUtil.formatTime(schedule.getDepartureTime()));*/
        //schedule.setName(schedule.getStartTerminal().getName() + " - " + schedule.getStopTerminal().getName() + " " + schedule.getDepartureString());
        return scheduleRepository.save(schedule);
    }

    public Schedule findById(long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist"));
    }

    public Schedule update(Schedule schedule) {
        Optional<Schedule> existing = scheduleRepository.findById(schedule.getId());
        if (existing.isPresent())
            return scheduleRepository.save(schedule);

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist");
    }

    public void delete(long id) {
        Optional<Schedule> existing = scheduleRepository.findById(id);
        if (existing.isPresent())
            scheduleRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist");
        }
    }
}
