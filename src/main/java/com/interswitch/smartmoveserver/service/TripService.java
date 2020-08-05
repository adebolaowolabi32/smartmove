package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Trip;
import com.interswitch.smartmoveserver.repository.TripRepository;
import com.interswitch.smartmoveserver.util.DateUtil;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.RandomUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TripService {


    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    TripRepository tripRepository;

    @Autowired
    PageUtil pageUtil;

    public List<Trip> getAll() {
        return tripRepository.findAll();
    }

    public Page<Trip> findAllPaginated(int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        return tripRepository.findAll(pageable);

    }

    public Trip save(Trip trip) {
        long id = trip.getId();
        boolean exists = tripRepository.existsById(id);

        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Trip already exists");

        trip.setReferenceNo(RandomUtil.getRandomNumber());
        trip.setArrivalObj(DateUtil.textToLocalDateTime(trip.getArrival()));
        trip.setDepartureObj(DateUtil.textToLocalDateTime(trip.getDeparture()));
        trip.setDeparture(DateUtil.formatDate(trip.getDepartureObj()));
        trip.setArrival(DateUtil.formatDate(trip.getArrivalObj()));
        trip.setName(trip.getRoute().getName() + "  " + trip.getDeparture());
        return tripRepository.save(trip);
    }

    public Trip findById(long id) {
        return tripRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip does not exist"));
    }

    public Trip update(Trip trip) {
        Optional<Trip> existing = tripRepository.findById(trip.getId());
        if (existing.isPresent())
            return tripRepository.save(trip);

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip does not exist");
    }

    public void delete(long id) {
        Optional<Trip> existing = tripRepository.findById(id);
        if (existing.isPresent())
            tripRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip does not exist");
        }
    }

    public List<Trip> findByDriverUsername(String username) {
        return tripRepository.findByDriverUsername(username);
    }

    public List<Trip> findByRouteName(String routeName) {
        return tripRepository.findByRouteName(routeName);
    }

    public List<Trip> findByRouteId(long id) {
        return tripRepository.findByRouteId(id);
    }

    public Page<Trip> findByRouteName(Pageable pageable, String routeName) {
        return tripRepository.findByRouteName(pageable, routeName);
    }

    public Page<Trip> findByRouteId(Pageable pageable, long id) {
        return tripRepository.findByRouteId(pageable, id);
    }


    public List<Trip> findByVehicleRegNo(String vehicleRegNo) {
        return tripRepository.findByVehicleRegNo(vehicleRegNo);
    }

    public Long countAll() {
        return tripRepository.count();
    }
}
