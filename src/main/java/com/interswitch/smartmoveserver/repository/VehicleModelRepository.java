package com.interswitch.smartmoveserver.repository;

import com.interswitch.smartmoveserver.model.VehicleMake;
import com.interswitch.smartmoveserver.model.VehicleModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleModelRepository extends CrudRepository<VehicleModel, Long> {
    List<VehicleModel> findAllByMake(VehicleMake make);
    List<VehicleModel> findAll();
}
