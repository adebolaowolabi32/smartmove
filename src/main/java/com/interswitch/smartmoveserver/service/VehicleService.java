package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class VehicleService {
    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    UserRepository userRepository;

    public Iterable<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    public Vehicle save(Vehicle vehicle) {
        long id = vehicle.getId();
        boolean exists = vehicleRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle already exists");
        return vehicleRepository.save(vehicle);
    }

    public Vehicle findById(long id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist"));
    }

    public Iterable<Vehicle> find(Enum.TransportMode type) {
        return vehicleRepository.findAllByType(type);
    }

    public Iterable<Vehicle> findAllByOwner(long owner) {
        Optional<User> user = userRepository.findById(owner);
        if(user.isPresent())
            return vehicleRepository.findAllByOwner(user.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner was not found");
    }

    public Vehicle update(Vehicle vehicle) {
        Optional<Vehicle> existing = vehicleRepository.findById(vehicle.getId());
        if(existing.isPresent())
            return vehicleRepository.save(vehicle);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist");
    }

    public void delete(long id) {
        Optional<Vehicle> existing = vehicleRepository.findById(id);
        if(existing.isPresent())
            vehicleRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist");
        }
    }

    public void activate(long vehicleId) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if(vehicleOptional.isPresent()){
            Vehicle vehicle = vehicleOptional.get();
            vehicle.setActive(true);
            vehicleRepository.save(vehicle);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist");
    }

    public void deactivate(long vehicleId) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if(vehicleOptional.isPresent()){
            Vehicle vehicle = vehicleOptional.get();
            vehicle.setActive(false);
            vehicleRepository.save(vehicle);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist");
    }
}
