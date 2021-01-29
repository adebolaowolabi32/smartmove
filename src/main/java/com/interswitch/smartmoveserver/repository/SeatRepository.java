
package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Seat;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface SeatRepository extends CrudRepository<Seat, Long> {

    List<Seat> findAll();

    Seat findById(String seatId);

    Set<Seat> findByVehicleId(long id);

    Seat findByVehicleIdAndSeatNo(long vehicleId, int seatNo);

}
