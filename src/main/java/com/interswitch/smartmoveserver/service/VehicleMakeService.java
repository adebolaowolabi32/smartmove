package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.VehicleMake;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.VehicleMakeRepository;
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
public class VehicleMakeService {
    @Autowired
    VehicleMakeRepository vehicleMakeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<VehicleMake> findAll() {
        return vehicleMakeRepository.findAll();
    }

    public VehicleMake save(VehicleMake vehicleMake) {
        String name = vehicleMake.getName();
        boolean exists = vehicleMakeRepository.existsByNameIgnoreCase(name);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle Make already exists");
        return vehicleMakeRepository.save(vehicleMake);
    }

    public VehicleMake findById(long id) {
        return vehicleMakeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Make does not exist"));
    }

    public VehicleMake update(VehicleMake vehicleMake) {
        Optional<VehicleMake> existing = vehicleMakeRepository.findById(vehicleMake.getId());
        if(existing.isPresent())
            return vehicleMakeRepository.save(vehicleMake);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Make does not exist");
    }

    public void delete(long id) {
        Optional<VehicleMake> existing = vehicleMakeRepository.findById(id);
        if(existing.isPresent())
            vehicleMakeRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Make does not exist");
        }
    }

    public boolean existsByNameIgnoreCase(String name) {
        return vehicleMakeRepository.existsByNameIgnoreCase(name);
    }
}
