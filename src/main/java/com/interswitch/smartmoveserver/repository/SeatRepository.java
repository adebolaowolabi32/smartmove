package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Seat;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SeatRepository extends CrudRepository<Seat, Long> {
    List<Seat> findAll();
      Seat findBySeatId(String seatId);
}
