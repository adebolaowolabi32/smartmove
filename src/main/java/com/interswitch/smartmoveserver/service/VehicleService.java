package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Document;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.VehicleRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    DocumentService documentService;

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Vehicle save(Vehicle vehicle) {

        long id = vehicle.getId();
        boolean exists = vehicleRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle already exists");

        if (vehicle.getPicture() != null) {
            Document doc = documentService.saveDocument(new Document(vehicle.getPicture()));
            vehicle.setPictureUrl(doc.getUrl());
        }
        Vehicle createdVehicle = vehicleRepository.save(vehicle);
        return createdVehicle;
    }

    public Vehicle save(Vehicle vehicle, Principal principal) {
        long id = vehicle.getId();
        boolean exists = vehicleRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle already exists");
        if (vehicle.getOwner() == null) {
            Optional<User> owner = userRepository.findByUsername(principal.getName());
            if (owner.isPresent()) vehicle.setOwner(owner.get());
        }
        return vehicleRepository.save(vehicle);
    }

    public Vehicle findById(long id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist"));
    }

    public Vehicle findById(Principal principal, long id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist"));
    }

    public List<Vehicle> findAllByOwner(User owner) {
        return vehicleRepository.findAllByOwner(owner);
    }

    public List<Vehicle> findAllByOwner(Long ownerId) {
        Optional<User> owner = userRepository.findById(ownerId);
        return vehicleRepository.findAllByOwner(owner.get());
    }

    public Vehicle update(Vehicle vehicle) {

        logger.info("Vehicle Picture===>" + vehicle.getPicture());

        Optional<Vehicle> existing = vehicleRepository.findById(vehicle.getId());

        if (existing.isPresent()) {
            if (vehicle.getPicture() != null) {
                logger.info("Vehicle Picture Not Null");
                Document doc = documentService.saveDocument(new Document(vehicle.getPicture()));
                vehicle.setPictureUrl(doc.getUrl());
            }
            return vehicleRepository.save(vehicle);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist");
    }

    public void delete(long id) {
        Optional<Vehicle> existing = vehicleRepository.findById(id);
        if (existing.isPresent())
            vehicleRepository.deleteById(id);
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist");
        }
    }

    public void activate(long vehicleId) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isPresent()) {
            Vehicle vehicle = vehicleOptional.get();
            vehicle.setEnabled(true);
            vehicleRepository.save(vehicle);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist");
    }

    public void deactivate(long vehicleId) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isPresent()) {
            Vehicle vehicle = vehicleOptional.get();
            vehicle.setEnabled(false);
            vehicleRepository.save(vehicle);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle does not exist");
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
}
