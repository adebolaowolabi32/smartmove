package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.model.VehicleCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleCategoryRepository extends CrudRepository<VehicleCategory, Long> {
    List<VehicleCategory> findAllByMode(Enum.TransportMode mode);
    List<VehicleCategory> findAllByOwner(User owner);
    Page<VehicleCategory> findAllByMode(Pageable pageable, Enum.TransportMode mode);
    Page<VehicleCategory> findAllByOwner(Pageable pageable, User owner);
    Long countByOwner(User owner);
    Page<VehicleCategory> findAll(Pageable pageable);
    List<VehicleCategory> findAll();
    boolean existsByName(String name);


}
