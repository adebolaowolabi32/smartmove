package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.request.DeviceConnection;
import com.interswitch.smartmoveserver.model.request.GetDeviceId;
import com.interswitch.smartmoveserver.model.response.DeviceConnectionResponse;
import com.interswitch.smartmoveserver.model.response.GetDeviceIdResponse;
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

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public DeviceConnectionResponse connectDevice(DeviceConnection deviceConnection) {
        Device device = new Device();
        device.setOwner(deviceConnection.getOwner());
        device.setDeviceId(deviceConnection.getDeviceId());
        device.setDeviceStatus(deviceConnection.getDeviceStatus());
        device.setHardwareVersion(deviceConnection.getHardwareVersion());
        device.setSoftwareVersion(deviceConnection.getSoftwareVersion());
        device.setFareType(deviceConnection.getFareType());
        deviceRepository.save(device);
        DeviceConnectionResponse deviceConnectionResponse = new DeviceConnectionResponse();
        deviceConnectionResponse.setMessageId(deviceConnection.getMessageId());
        deviceConnectionResponse.setTimeDate(LocalDateTime.now().toString());
        deviceConnectionResponse.setResponseCode("00");
        return deviceConnectionResponse;
    }
    
    public GetDeviceIdResponse getDeviceId(GetDeviceId getDeviceId){
        GetDeviceIdResponse getDeviceIdResponse = new GetDeviceIdResponse();
        getDeviceIdResponse.setDeviceId(UUID.randomUUID().toString());
        getDeviceIdResponse.setMessageId(getDeviceId.getMessageId());
        getDeviceIdResponse.setResponseCode("00");
        return getDeviceIdResponse;
    }

    public Page<Device> findAllPaginatedByType(Principal principal, Long owner, Enum.DeviceType type, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.get().getRole()))
                return deviceRepository.findAllByTypeAndOwner(pageable, type, user.get());
            else
                return deviceRepository.findAllByType(pageable, type);
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                Optional<User> ownerUser = userRepository.findById(owner);
                if (!ownerUser.isPresent())
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found");
                return deviceRepository.findAllByTypeAndOwner(pageable, type, ownerUser.get());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public Page<Device> findAllPaginated(Principal principal, Long owner, int page, int size) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (!user.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found");

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.get().getRole()))
                return deviceRepository.findAllByOwner(pageable, user.get());
            else
                return deviceRepository.findAll(pageable);
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                Optional<User> ownerUser = userRepository.findById(owner);
                if (!ownerUser.isPresent())
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user not found");
                return deviceRepository.findAllByOwner(pageable, ownerUser.get());
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

    public Device save(Device device, Principal principal) {
        long id = device.getId();
        boolean exists = deviceRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Device already exists");
        if(device.getOwner() == null) {
            Optional<User> owner = userRepository.findByUsername(principal.getName());
            if(owner.isPresent()) device.setOwner(owner.get());
        }
        return deviceRepository.save(device);
    }
    
    public Device findById(long id) {
        return deviceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device does not exist"));
    }

    public List<Device> find(Enum.DeviceType type) {
        return deviceRepository.findAllByType(type);
    }

    public List<Device> findAllByOwner(User owner) {
        return deviceRepository.findAllByOwner(owner);
    }

    public List<Device> findAllByOwner(long owner) {
        return deviceRepository.findAll();//urgent
    }

    public Device update(Device device) {
        Optional<Device> existing = deviceRepository.findById(device.getId());
        if(existing.isPresent())
            return deviceRepository.save(device);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device does not exist");
    }

    public void assignToVehicle(long deviceId, long vehicleId) {
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        if(deviceOptional.isPresent()){
            Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
            if(vehicleOptional.isPresent()){
                Vehicle vehicle = vehicleOptional.get();
                vehicle.setDevice(deviceOptional.get());
                vehicleRepository.save(vehicle);
            }
            else throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Vehicle does not exist");
        }
        else throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Device does not exist");
    }

    public void assignToAgent(long deviceId, long agentId) {
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        if(deviceOptional.isPresent()){
            Optional<User> userOptional = userRepository.findById(agentId);
            if(userOptional.isPresent()){
                Device device = deviceOptional.get();
                device.setOwner(userOptional.get());
                deviceRepository.save(device);
            }
            else throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Agent does not exist");
        }
        else throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Device does not exist");
    }

    public void delete(long id) {
        Optional<Device> existing = deviceRepository.findById(id);
        if(existing.isPresent())
            deviceRepository.deleteById(id);
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device does not exist");
        }
    }

    public String fundDevice(Principal principal, FundDevice fundDevice) {
        double amount = fundDevice.getAmount();
        Wallet wallet = walletService.findByOwner(principal.getName());
        Device device = findById(fundDevice.getDeviceId());
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
