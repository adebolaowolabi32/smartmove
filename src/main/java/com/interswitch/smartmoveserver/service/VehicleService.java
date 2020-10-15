package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.VehicleRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class VehicleService {
    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    VehicleCategoryService vehicleCategoryService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Vehicle save(Vehicle vehicle, Principal principal) {
        long id = vehicle.getId();
        boolean exists = vehicleRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle already exists");
        if (vehicle.getOwner() == null) {
            User owner = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));
            vehicle.setOwner(owner);
        }
        return vehicleRepository.save(buildVehicle(vehicle));
    }

    public Vehicle findById(long id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist"));
    }

    public Vehicle findById(long id, Principal principal) {
        return vehicleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist"));
    }

    public Vehicle update(Vehicle vehicle, Principal principal) {
        Optional<Vehicle> existing = vehicleRepository.findById(vehicle.getId());

        if (existing.isPresent()) {
            if (vehicle.getOwner() == null) {
                User owner = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));
                vehicle.setOwner(owner);
            }
            return vehicleRepository.save(buildVehicle(vehicle));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist");
    }

    public void delete(long id, Principal principal) {
        Optional<Vehicle> existing = vehicleRepository.findById(id);
        if (existing.isPresent())
            vehicleRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist");
        }
    }

    public long countByOwner(User user) {
        return vehicleRepository.countByOwner(user);
    }

    public long countAll() {
        return vehicleRepository.count();
    }

    public Page<Vehicle> findAllPaginated(Principal principal, Long owner, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.get().getRole()))
                return vehicleRepository.findAllByOwner(pageable, user.get());
            else
                return vehicleRepository.findAll(pageable);
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                Optional<User> ownerUser = userRepository.findById(owner);
                if (!ownerUser.isPresent())
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found");
                return vehicleRepository.findAllByOwner(pageable, ownerUser.get());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }
    private Vehicle buildVehicle(Vehicle vehicle) {
        VehicleCategory vehicleCategory = vehicle.getCategory();
        if(vehicleCategory != null)
            vehicle.setCategory(vehicleCategoryService.findById(vehicleCategory.getId()));

        Device device = vehicle.getDevice();
        if(device != null)
            vehicle.setDevice(deviceService.findById(device.getId()));
        return vehicle;
    }
}
