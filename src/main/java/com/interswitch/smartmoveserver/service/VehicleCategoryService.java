package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.repository.VehicleCategoryRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
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
@Service
public class VehicleCategoryService {
    @Autowired
    VehicleCategoryRepository vehicleCategoryRepository;

    @Autowired
    VehicleMakeService vehicleMakeService;

    @Autowired
    VehicleModelService vehicleModelService;

    @Autowired
    UserService userService;

    @Autowired
    DocumentService documentService;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public List<VehicleCategory> findAll() {
        return vehicleCategoryRepository.findAll();
    }

    public List<VehicleCategory> findAll(Long owner, String principal) {
        User user = userService.findByUsername(principal);
        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                return vehicleCategoryRepository.findAllByOwner(user);
            } else {
                return vehicleCategoryRepository.findAll();
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                return vehicleCategoryRepository.findAllByOwner(ownerUser);
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public PageView<VehicleCategory> findAllPaginated(Long owner, int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        User user = userService.findByUsername(principal);
        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                Page<VehicleCategory> pages = vehicleCategoryRepository.findAllByOwner(pageable, user);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            } else {
                Page<VehicleCategory> pages = vehicleCategoryRepository.findAll(pageable);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                Page<VehicleCategory> pages = vehicleCategoryRepository.findAllByOwner(pageable, ownerUser);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public VehicleCategory save(VehicleCategory vehicleCategory, String principal) {
        String name = vehicleCategory.getName();
        boolean exists = vehicleCategoryRepository.existsByName(name);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle Category with name: " + name + " already exists");
        if(vehicleCategory.getOwner() == null) {
            User owner = userService.findByUsername(principal);
            vehicleCategory.setOwner(owner);
        }
        if (vehicleCategory.getPicture().getSize() > 0) {
            Document doc = documentService.saveDocument(new Document(vehicleCategory.getPicture()));
            vehicleCategory.setPictureUrl(doc.getUrl());
        }
        return vehicleCategoryRepository.save(buildVehicleCategory(vehicleCategory));
    }

    public VehicleCategory findById(long id) {
        return vehicleCategoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Category does not exist"));
    }

    public VehicleCategory findById(long id, String principal) {
        return vehicleCategoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Category does not exist"));
    }

    //findAllByMode

    public List<VehicleCategory> findByOwner(String username) {
        User owner = userService.findByUsername(username);
        return vehicleCategoryRepository.findAllByOwner(owner);
    }


    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public VehicleCategory update(VehicleCategory vehicleCategory, String principal) {
        Optional<VehicleCategory> existing = vehicleCategoryRepository.findById(vehicleCategory.getId());
        if(existing.isPresent())
        {
            if(vehicleCategory.getOwner() == null) {
                User owner = userService.findByUsername(principal);
                vehicleCategory.setOwner(owner);
            }
            if (vehicleCategory.getPicture().getSize() > 0) {
                Document doc = documentService.saveDocument(new Document(vehicleCategory.getPicture()));
                vehicleCategory.setPictureUrl(doc.getUrl());
            }
            return vehicleCategoryRepository.save(vehicleCategory);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Category does not exist");
    }

    public void delete(long id, String principal) {
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
