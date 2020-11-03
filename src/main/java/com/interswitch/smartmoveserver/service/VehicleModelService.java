package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.VehicleMake;
import com.interswitch.smartmoveserver.model.VehicleModel;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.VehicleModelRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * @author adebola.owolabi
 */
@Service
public class VehicleModelService {
    @Autowired
    VehicleModelRepository vehicleModelRepository;

    @Autowired
    VehicleMakeService vehicleMakeService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<VehicleModel> findAll() {
        return vehicleModelRepository.findAll();
    }

    public VehicleModel save(VehicleModel vehicleModel) {
        String name = vehicleModel.getName();
        boolean exists = vehicleModelRepository.existsByNameIgnoreCase(name);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle Model already exists");
        return vehicleModelRepository.save(vehicleModel);
    }

    public VehicleModel findById(long id) {
        return vehicleModelRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Model does not exist"));
    }

    public List<VehicleModel> findAllByMake(long makeId){
        VehicleMake make = vehicleMakeService.findById(makeId);
        return vehicleModelRepository.findAllByMake(make);
    }

    public VehicleModel update(VehicleModel vehicleModel) {
        Optional<VehicleModel> existing = vehicleModelRepository.findById(vehicleModel.getId());
        if(existing.isPresent())
            return vehicleModelRepository.save(vehicleModel);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Model does not exist");
    }

    public void delete(long id) {
        Optional<VehicleModel> existing = vehicleModelRepository.findById(id);
        if(existing.isPresent())
            vehicleModelRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Model does not exist");
        }
    }
}
