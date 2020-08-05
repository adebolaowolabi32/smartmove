package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Seat;
import org.springframework.data.repository.CrudRepository;

public interface SeatRepository extends CrudRepository<Seat, Long> {
}
