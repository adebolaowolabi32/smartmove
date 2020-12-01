package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.audit.AuditableActionStatusImpl;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.*;
import com.interswitch.smartmoveserver.model.dto.DeviceDto;
import com.interswitch.smartmoveserver.model.view.FundDevice;
import com.interswitch.smartmoveserver.repository.DeviceRepository;
import com.interswitch.smartmoveserver.util.FileParser;
import com.interswitch.smartmoveserver.util.PageUtil;
import com.interswitch.smartmoveserver.util.SecurityUtil;
import com.interswitchng.audit.annotation.Audited;
import com.interswitchng.audit.model.AuditableAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        User user = userService.findByUsername(principal);
        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                Page<Device> pages = deviceRepository.findAllByTypeAndOwner(pageable, type, user);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            else {
                Page<Device> pages = deviceRepository.findAllByType(pageable, type);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                Page<Device> pages = deviceRepository.findAllByTypeAndOwner(pageable, type, ownerUser);
                return new PageView<>(pages.getTotalElements(), pages.getContent());

            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public PageView<Device> findAllPaginated(Long owner, int page, int size, String principal) {
        PageRequest pageable = pageUtil.buildPageRequest(page, size);
        User user = userService.findByUsername(principal);

        if (owner == 0) {
            if (securityUtil.isOwnedEntity(user.getRole())) {
                Page<Device> pages = deviceRepository.findAllByOwner(pageable, user);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            else {
                Page<Device> pages = deviceRepository.findAll(pageable);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
        } else {
            if (securityUtil.isOwner(principal, owner)) {
                User ownerUser = userService.findById(owner);
                Page<Device> pages = deviceRepository.findAllByOwner(pageable, ownerUser);
                return new PageView<>(pages.getTotalElements(), pages.getContent());
            }
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have sufficient rights to this resource.");
        }
    }

    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Device save(Device device) {
        return deviceRepository.save(device);
    }


    @Audited(auditableAction = AuditableAction.CREATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Device save(Device device, String principal) {
        String name = device.getName();
        boolean exists = deviceRepository.existsByName(name);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Device with name: " + name + " already exists");
        if(device.getOwner() == null) {
            User owner = userService.findByUsername(principal);
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

    @Audited(auditableAction = AuditableAction.UPDATE, auditableActionClass = AuditableActionStatusImpl.class)
    public Device update(Device device, String principal) {
        Optional<Device> existing = deviceRepository.findById(device.getId());
        if(existing.isPresent()){
            if(device.getOwner() == null) {
                User owner = userService.findByUsername(principal);
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
        User owner = userService.findByUsername(principal);
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
            transfer1.setOwner(owner);
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

    public boolean upload(MultipartFile file, String principal) throws IOException {
        User owner = userService.findByUsername(principal);
        List<Device> savedDevices = new ArrayList<>();
        if(file.getSize()>1){
            FileParser<DeviceDto> fileParser = new FileParser<>();
            List<DeviceDto> deviceDtoList = fileParser.parseFileToEntity(file, DeviceDto.class);
            deviceDtoList.forEach(deviceDto -> {
                savedDevices.add(save(mapToDevice(deviceDto, owner), principal));
            });
        }
        return savedDevices.size()>1;
    }

    private Device mapToDevice(DeviceDto deviceDto, User owner){

        return Device.builder()
                .balance(0)
                .batteryPercentage(deviceDto.getBatteryPercentage())
                .deviceId(deviceDto.getDeviceId())
                .enabled(isEnabled(deviceDto.getEnabled()))
                .name(deviceDto.getName())
                .hardwareVersion(deviceDto.getHardwareVersion())
                .softwareVersion(deviceDto.getSoftwareVersion())
                .deviceStatus(convertToDeviceStatus(deviceDto.getDeviceStatus()))
                .fareType(convertToFareType(deviceDto.getFareType()))
                .type(convertToDeviceType(deviceDto.getDeviceType()))
                .owner(owner)
                .build();
    }

    private boolean isEnabled(String enabledStatus){
        return enabledStatus.equalsIgnoreCase("true") || enabledStatus.startsWith("true");
    }

    private Enum.DeviceStatus convertToDeviceStatus(String status){
        //CONNECTED, DISCONNECTED, BATTERY_LOW, EMERGENCY
        Enum.DeviceStatus deviceStatus = null;
        try{
             deviceStatus = Enum.DeviceStatus.valueOf(status.toUpperCase());
        }catch(IllegalArgumentException ex){

        }

        return deviceStatus;
    }

    private Enum.FareType convertToFareType(String fareType){
        //FIXED, VARIABLE
        return Enum.FareType.valueOf(fareType.toUpperCase());
    }

    private Enum.DeviceType convertToDeviceType(String fareType){
        // READER, VALIDATOR, READER_VALIDATOR
        return Enum.DeviceType.valueOf(fareType.toUpperCase());
    }

}
