package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {
    List<Vehicle> findAllByOwner(User owner);
    Page<Vehicle> findAllByOwner(Pageable pageable, User owner);
    Long countByOwner(User owner);
    Page<Vehicle> findAll(Pageable pageable);
    List<Vehicle> findAll();
}
