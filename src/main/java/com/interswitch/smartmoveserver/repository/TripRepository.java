package com.interswitch.smartmoveserver.repository;


import com.interswitch.smartmoveserver.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends CrudRepository<Trip, Long> {

     Trip findByReferenceNo(String referenceNo);
     List<Trip> findByDriverUsername(String username);
     List<Trip> findByRouteName(String routeName);
     List<Trip> findByRouteId(long id);
     Page<Trip> findByRouteName(Pageable pageable,String routeName);
     Page<Trip> findByRouteId(Pageable pageable,long id);
     List<Trip> findByVehicleRegNo(String vehicleRegNo);
     Page<Trip> findAll(Pageable pageable);
     List<Trip> findAll();

}
