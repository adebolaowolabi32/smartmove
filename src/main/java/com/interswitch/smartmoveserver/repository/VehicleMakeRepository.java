package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.VehicleMake;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Created by adebola.owolabi on 7/27/2020
 */
@Repository
public interface VehicleMakeRepository extends CrudRepository<VehicleMake, Long> {
    List<VehicleMake> findAll();
    boolean existsByNameIgnoreCase(String name);
}
