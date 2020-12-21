package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Schedule;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {

    Page<Schedule> findAll(Pageable pageable);

    List<Schedule> findAll();

    List<Schedule> findAllByOwner(User owner);
}
