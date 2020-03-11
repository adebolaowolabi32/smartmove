package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * @author adebola.owolabi
 */
@Service
public class VehicleService {
    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    UserRepository userRepository;

    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    public Page<Vehicle> findAllPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return vehicleRepository.findAll(pageable);
    }

    public Vehicle save(Vehicle vehicle) {
        long id = vehicle.getId();
        boolean exists = vehicleRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle already exists");
        return vehicleRepository.save(vehicle);
    }

    public Vehicle save(Vehicle vehicle, Principal principal) {
        long id = vehicle.getId();
        boolean exists = vehicleRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle already exists");
        if(vehicle.getOwner() == null) {
            Optional<User> owner = userRepository.findByUsername(principal.getName());
            if(owner.isPresent()) vehicle.setOwner(owner.get());
        }
        return vehicleRepository.save(vehicle);
    }

    public Vehicle findById(long id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist"));
    }

    public List<Vehicle> find(Enum.TransportMode type) {
        return vehicleRepository.findAllByType(type);
    }

    public List<Vehicle> findAllByOwner(User owner) {
        return vehicleRepository.findAllByOwner(owner);
    }

    public List<Vehicle> findAllByOwner(long owner) {
        return vehicleRepository.findAll();//urgent
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
            vehicle.setEnabled(true);
            vehicleRepository.save(vehicle);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist");
    }

    public void deactivate(long vehicleId) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if(vehicleOptional.isPresent()){
            Vehicle vehicle = vehicleOptional.get();
            vehicle.setEnabled(false);
            vehicleRepository.save(vehicle);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist");
    }

    public Long countByOwner(User user){
        return vehicleRepository.countByOwner(user);
    }

    public Long countAll(){
        return vehicleRepository.count();
    }
}
