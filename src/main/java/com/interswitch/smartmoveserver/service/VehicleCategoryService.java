package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.VehicleCategory;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.VehicleCategoryRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
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
public class VehicleCategoryService {
    @Autowired
    VehicleCategoryRepository vehicleCategoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<VehicleCategory> findAll() {
        return vehicleCategoryRepository.findAll();
    }

    public VehicleCategory save(VehicleCategory vehicleCategory) {
        long id = vehicleCategory.getId();
        boolean exists = vehicleCategoryRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle Category already exists");
        return vehicleCategoryRepository.save(vehicleCategory);
    }

    public VehicleCategory save(VehicleCategory vehicleCategory, Principal principal) {
        long id = vehicleCategory.getId();
        boolean exists = vehicleCategoryRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle Category already exists");
        if(vehicleCategory.getOwner() == null) {
            Optional<User> owner = userRepository.findByUsername(principal.getName());
            if(owner.isPresent()) vehicleCategory.setOwner(owner.get());
        }
        return vehicleCategoryRepository.save(vehicleCategory);
    }

    public VehicleCategory findById(long id) {
        return vehicleCategoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Category does not exist"));
    }

    public VehicleCategory findById(Principal principal, long id) {
        return vehicleCategoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Category does not exist"));
    }

    public List<VehicleCategory> findAllByMode(Enum.TransportMode mode) {
        return vehicleCategoryRepository.findAllByMode(mode);
    }

    public List<VehicleCategory> findAllByOwner(User owner) {
        return vehicleCategoryRepository.findAllByOwner(owner);
    }

    public List<VehicleCategory> findAllByOwner(Long ownerId) {
        Optional<User> owner = userRepository.findById(ownerId);
        return vehicleCategoryRepository.findAllByOwner(owner.get());
    }

    public VehicleCategory update(VehicleCategory vehicleCategory) {
        Optional<VehicleCategory> existing = vehicleCategoryRepository.findById(vehicleCategory.getId());
        if(existing.isPresent())
            return vehicleCategoryRepository.save(vehicleCategory);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Category does not exist");
    }

    public void delete(long id) {
        Optional<VehicleCategory> existing = vehicleCategoryRepository.findById(id);
        if(existing.isPresent())
            vehicleCategoryRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Category does not exist");
        }
    }

    public long countByOwner(User user){
        return vehicleCategoryRepository.countByOwner(user);
    }

    public long countAll(){
        return vehicleCategoryRepository.count();
    }

    public Page<VehicleCategory> findAllPaginated(Principal principal, Long owner, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if(!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if(owner == 0) {
            if (securityUtil.isOwnedEntity(user.get().getRole()))
                return vehicleCategoryRepository.findAllByOwner(pageable, user.get());
            else
                return vehicleCategoryRepository.findAll(pageable);
        }
        else {
            if(securityUtil.isOwner(principal, owner)){
                Optional<User> ownerUser = userRepository.findById(owner);
                if(!ownerUser.isPresent())
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found");
                return vehicleCategoryRepository.findAllByOwner(pageable, ownerUser.get());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }
}
