package com.interswitch.smartmoveserver.repository;


import com.interswitch.smartmoveserver.model.Trip;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends CrudRepository<Trip, Long> {

    Trip findByReferenceNo(String referenceNo);

    List<Trip> findByDriverUsername(String username);

    List<Trip> findByVehicleRegNo(String vehicleRegNo);

    Page<Trip> findAllByOwner(Pageable pageable, User owner);

    Page<Trip> findAll(Pageable pageable);

    List<Trip> findAll();

    List<Trip> findAllByOwner(User user);

    Long countByOwner(User owner);
}