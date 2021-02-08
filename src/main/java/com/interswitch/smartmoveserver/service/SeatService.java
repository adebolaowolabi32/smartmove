package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Seat;
import com.interswitch.smartmoveserver.repository.SeatRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author adebola.owolabi
 */
@Service
public class SeatService {
    @Autowired
    SeatRepository seatRepository;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<Seat> findAll() {
        return seatRepository.findAll();
    }

    public Seat save(Seat seat) {
        long id = seat.getId();
        boolean exists = seatRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Seat already exists");
        return seatRepository.save(seat);
    }

    public Seat findById(long id) {
        return seatRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat does not exist"));
    }

    public Seat update(Seat seat) {
        Optional<Seat> existing = seatRepository.findById(seat.getId());
        if (existing.isPresent())
            return seatRepository.save(seat);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat does not exist");
    }

    public void delete(long id) {
        Optional<Seat> existing = seatRepository.findById(id);
        if (existing.isPresent())
            seatRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat does not exist");
        }
    }

    public Set<Seat> findSeatsByVehicleId(long vehicleId){
       return seatRepository.findByVehicleId(vehicleId); }

}
