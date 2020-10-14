package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.*;
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
    VehicleMakeService vehicleMakeService;

    @Autowired
    VehicleModelService vehicleModelService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DocumentService documentService;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<VehicleCategory> findAll() {
        return vehicleCategoryRepository.findAll();
    }

    public VehicleCategory save(VehicleCategory vehicleCategory, Principal principal) {
        long id = vehicleCategory.getId();
        boolean exists = vehicleCategoryRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle Category already exists");
        if(vehicleCategory.getOwner() == null) {
            User owner = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));
            vehicleCategory.setOwner(owner);
        }
        if (vehicleCategory.getPicture() != null && vehicleCategory.getPicture().getSize() > 0) {
            Document doc = documentService.saveDocument(new Document(vehicleCategory.getPicture()));
            vehicleCategory.setPictureUrl(doc.getUrl());
        }
        return vehicleCategoryRepository.save(buildVehicleCategory(vehicleCategory));
    }

    public VehicleCategory findById(long id) {
        return vehicleCategoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Category does not exist"));
    }

    public VehicleCategory findById(long id, Principal principal) {
        return vehicleCategoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Category does not exist"));
    }

    //findAllByMode

    public VehicleCategory update(VehicleCategory vehicleCategory, Principal principal) {
        Optional<VehicleCategory> existing = vehicleCategoryRepository.findById(vehicleCategory.getId());
        if(existing.isPresent())
        {
            if(vehicleCategory.getOwner() == null) {
                User owner = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));
                vehicleCategory.setOwner(owner);
            }
            if (vehicleCategory.getPicture() != null && vehicleCategory.getPicture().getSize() > 0) {
                Document doc = documentService.saveDocument(new Document(vehicleCategory.getPicture()));
                vehicleCategory.setPictureUrl(doc.getUrl());
            }
            return vehicleCategoryRepository.save(vehicleCategory);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Category does not exist");
    }

    public void delete(long id, Principal principal) {
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

    private VehicleCategory buildVehicleCategory(VehicleCategory vehicleCategory) {
        VehicleMake vehicleMake = vehicleCategory.getMake();
        if(vehicleMake != null)
            vehicleCategory.setMake(vehicleMakeService.findById(vehicleMake.getId()));

        VehicleModel vehicleModel = vehicleCategory.getModel();
        if(vehicleModel != null)
            vehicleCategory.setModel(vehicleModelService.findById(vehicleModel.getId()));
        return vehicleCategory;
    }
}
