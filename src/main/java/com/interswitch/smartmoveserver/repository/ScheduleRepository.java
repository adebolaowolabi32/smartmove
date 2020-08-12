package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Schedule;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Created by adebola.owolabi on 8/7/2020
 */
@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {

    Page<Schedule> findAll(Pageable pageable);

    List<Schedule> findAll();

    List<Schedule> findByOwner(User owner);
   /* List<Schedule> findByTerminalName(String terminalName);

    Page<Schedule> findByTerminalName(Pageable pageable, String terminalName);*/
}
