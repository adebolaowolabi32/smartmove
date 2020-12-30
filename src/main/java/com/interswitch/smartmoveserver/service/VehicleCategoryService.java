package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.repository.SeatRepository;
import com.interswitch.smartmoveserver.repository.VehicleCategoryRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author adebola.owolabi
 */
@Slf4j
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
    SeatRepository seatRepository;

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

   // @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public VehicleCategory save(VehicleCategory vehicleCategory, String principal) {
        log.info("principal===>"+principal);
        String name = vehicleCategory.getName();
        boolean exists = vehicleCategoryRepository.existsByName(name);
        log.info("vehicleCategoryExists ===>"+exists);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle Category with name: " + name + " already exists");

        if(vehicleCategory.getOwner() == null) {
            try {

                User owner = userService.findByUsername(principal);
                vehicleCategory.setOwner(owner);

            }catch(ResponseStatusException ex){
                log.info("User with username "+principal+" cannot be found in database.");
            }
        }

        log.info("Owner is not null ===>");

        if (vehicleCategory.getPicture().getSize() > 0) {
            Document doc = documentService.saveDocument(new Document(vehicleCategory.getPicture()));
            vehicleCategory.setPictureUrl(doc.getUrl());
        }

        log.info("wanna save vehicle category ===>");
        VehicleCategory vehicle =  vehicleCategoryRepository.save(buildVehicleCategory(vehicleCategory));
        log.info("finished creating vehicle category ===>");

        return  createSeats(vehicle);
    }

    public VehicleCategory findById(long id) {
        return vehicleCategoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Category does not exist"));
    }

    public VehicleCategory findById(long id, String principal) {
        return vehicleCategoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Category does not exist"));
    }

    public List<VehicleCategory> findByOwner(String username) {
        User owner = userService.findByUsername(username);
        if (owner.getRole() == Enum.Role.ISW_ADMIN)
            return vehicleCategoryRepository.findAll();
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
        log.info("building vehicle category");
        VehicleMake vehicleMake = vehicleCategory.getMake();
        if(vehicleMake != null)
            vehicleCategory.setMake(vehicleMakeService.findById(vehicleMake.getId()));

        VehicleModel vehicleModel = vehicleCategory.getModel();
        if(vehicleModel != null)
            vehicleCategory.setModel(vehicleModelService.findById(vehicleModel.getId()));
        return vehicleCategory;
    }

    private static int getIntegerPart(float value) {
        float fractionalPart = value % 1;
        float integralPart = value - fractionalPart;
        return  Math.round(integralPart);
    }

    private VehicleCategory createSeats(VehicleCategory vehicle){
        log.info("Wanna create seat");

        Set<Seat> seats = new HashSet<>();

        for(int i=1;i<=vehicle.getCapacity();i++){

            Seat seat  = new Seat();
            seat.setSeatNo(i);
            seat.setAvailable(true);
            seat.setVehicle(vehicle);
            log.info("Wanna create seat===>"+i);
            Seat createdSeat = seatRepository.save(seat);
            seats.add(createdSeat);
            log.info("added to seat===>");
        }
        log.info("wanna set seats for vehicle");
        vehicle.setSeats(seats);
        log.info("done setting seats for vehicle,wanna call save");
        //vehicleCategoryRepository.save(vehicle);
        return vehicle;
    }
}