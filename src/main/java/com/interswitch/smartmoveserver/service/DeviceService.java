package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.model.request.DeviceConnection;
import com.interswitch.smartmoveserver.model.request.GetDeviceId;
import com.interswitch.smartmoveserver.model.response.DeviceConnectionResponse;
import com.interswitch.smartmoveserver.model.response.GetDeviceIdResponse;
import com.interswitch.smartmoveserver.repository.DeviceRepository;
import com.interswitch.smartmoveserver.repository.UserRepository;
import com.interswitch.smartmoveserver.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Page<Device> findAllPaginated(int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        return deviceRepository.findAll(pageable);
    }

    public List<Device> getAll() {
        return deviceRepository.findAll();
    }

    public Device save(Device device) {
        long id = device.getId();
        boolean exists = deviceRepository.existsById(id);
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "Device already exists");
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

    public void activate(long deviceId) {
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        if(deviceOptional.isPresent()){
            Device device = deviceOptional.get();
            device.setActive(true);
            deviceRepository.save(device);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device does not exist");
    }

    public void deactivate(long deviceId) {
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        if(deviceOptional.isPresent()){
            Device device = deviceOptional.get();
            device.setActive(false);
            deviceRepository.save(device);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device does not exist");
    }

    public Long countByOwner(User user){
        return deviceRepository.countByOwner(user);
    }
}
