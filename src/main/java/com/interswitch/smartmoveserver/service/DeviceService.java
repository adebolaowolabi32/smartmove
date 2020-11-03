package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.view.FundDevice;
import com.interswitch.smartmoveserver.repository.DeviceRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.VehicleRepository;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author adebola.owolabi
 */
@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransferService transferService;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    PageUtil pageUtil;

    public PageView<Device> findAllPaginatedByType(Long owner, Enum.DeviceType type, int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal);
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.get().getRole())) {
                Page<Device> pages = deviceRepository.findAllByTypeAndOwner(pageable, type, user.get());
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            else {
                Page<Device> pages = deviceRepository.findAllByType(pageable, type);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                Optional<User> ownerUser = userRepository.findById(owner);
                if (!ownerUser.isPresent())
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found");
                Page<Device> pages = deviceRepository.findAllByTypeAndOwner(pageable, type, ownerUser.get());
                return new PageView<>(pages.getTotalElements(), pages.getContent());

            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public PageView<Device> findAllPaginated(Long owner, int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal);
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.get().getRole())) {
                Page<Device> pages = deviceRepository.findAllByOwner(pageable, user.get());
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            else {
                Page<Device> pages = deviceRepository.findAll(pageable);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                Optional<User>ownerUser = userRepository.findById(owner);
                if (!ownerUser.isPresent())
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found");
                Page<Device> pages = deviceRepository.findAllByOwner(pageable, ownerUser.get());
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    public Device save(Device device) {
        return deviceRepository.save(device);
    }

    public Device save(Device device, String principal) {
        String name = device.getName();
        boolean exists = deviceRepository.existsByName(name);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Device with name: " + name + " already exists");
        if(device.getOwner() == null) {
            User owner = userRepository.findByUsername(principal).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));
            device.setOwner(owner);
        }
        return deviceRepository.save(device);
    }

    public Device findById(long id) {
        return deviceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device does not exist"));
    }

    public Device findById(long id, String principal) {
        return deviceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device does not exist"));
    }

    public List<Device> find(Enum.DeviceType type) {
        return deviceRepository.findAllByType(type);
    }

    public Device update(Device device, String principal) {
        Optional<Device> existing = deviceRepository.findById(device.getId());
        if(existing.isPresent()){
            if(device.getOwner() == null) {
                User owner = userRepository.findByUsername(principal).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user does not exist"));
                device.setOwner(owner);
            }
            return deviceRepository.save(device);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device does not exist");
    }

    public void delete(long id, String principal) {
        Optional<Device> existing = deviceRepository.findById(id);
        if(existing.isPresent())
            deviceRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device does not exist");
        }
    }

    public String fundDevice(String principal, FundDevice fundDevice) {
        double amount = fundDevice.getAmount();
        Wallet wallet = walletService.findByOwner(principal);
        Device device = findById(fundDevice.getDeviceId(), principal);
        double deviceBalance = device.getBalance();
        double walletBalance = wallet.getBalance();
        if (walletBalance >= amount) {
            walletBalance = walletBalance - amount;
            deviceBalance = deviceBalance + amount;
            wallet.setBalance(walletBalance);
            device.setBalance(deviceBalance);
            walletService.save(wallet);
            save(device);
            Transfer transfer1 = new Transfer();
            transfer1.setAmount(amount);
            transfer1.setRecipient("Device - " + fundDevice.getDeviceId());
            transfer1.setWallet(wallet);
            transfer1.setTransferDateTime(LocalDateTime.now());
            transferService.save(transfer1);
        }
        return "Insufficient funds";
        //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Insufficient funds");
    }

    public void activate(long deviceId) {
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        if(deviceOptional.isPresent()){
            Device device = deviceOptional.get();
            device.setEnabled(true);
            deviceRepository.save(device);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device does not exist");
    }

    public void deactivate(long deviceId) {
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        if(deviceOptional.isPresent()){
            Device device = deviceOptional.get();
            device.setEnabled(false);
            deviceRepository.save(device);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device does not exist");
    }

    public Long countByOwner(User user){
        return deviceRepository.countByOwner(user);
    }

    public Long countAll(){
        return deviceRepository.count();
    }

    public Long countByType(Enum.DeviceType deviceType){
        return deviceRepository.countByType(deviceType);
    }

    public Long countByTypeAndOwner(Enum.DeviceType deviceType, User owner){
        return deviceRepository.countByTypeAndOwner(deviceType, owner);
    }
}
