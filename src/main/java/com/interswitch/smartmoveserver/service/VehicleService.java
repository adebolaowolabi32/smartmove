package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.*;
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
    UserService userService;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Vehicle save(Vehicle vehicle, String principal) {
        String name = vehicle.getName();
        boolean exists = vehicleRepository.existsByName(name);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle with name: " + name + " already exists");
        if (vehicle.getOwner() == null) {
            User owner = userService.findByUsername(principal);
            vehicle.setOwner(owner);
        }
        return vehicleRepository.save(buildVehicle(vehicle));
    }

    public Vehicle findById(long id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist"));
    }

    public Vehicle findById(long id, String principal) {
        return vehicleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist"));
    }

    public Vehicle findByRegNo(String regNo) {
        return vehicleRepository.findByRegNo(regNo);
    }

    public List<Vehicle> findByOwner(String username) {
        User owner = userService.findByUsername(username);
        return vehicleRepository.findAllByOwner(owner);
    }

    public Vehicle update(Vehicle vehicle, String principal) {
        Optional<Vehicle> existing = vehicleRepository.findById(vehicle.getId());

        if (existing.isPresent()) {
            if (vehicle.getOwner() == null) {
                User owner = userService.findByUsername(principal);
                vehicle.setOwner(owner);
            }
            return vehicleRepository.save(buildVehicle(vehicle));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist");
    }

    public void delete(long id, String principal) {
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

    public PageView<Vehicle> findAllPaginated(Long owner, int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        User user = userService.findByUsername(principal);
        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                Page<Vehicle> pages = vehicleRepository.findAllByOwner(pageable, user);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            else {
                Page<Vehicle> pages = vehicleRepository.findAll(pageable);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                Page<Vehicle> pages = vehicleRepository.findAllByOwner(pageable, ownerUser);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
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
