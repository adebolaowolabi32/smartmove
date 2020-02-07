package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.request.DeviceConnection;
import com.interswitch.smartmoveserver.model.request.GetDeviceId;
import com.interswitch.smartmoveserver.model.request.GetOwnerId;
import com.interswitch.smartmoveserver.model.response.DeviceConnectionResponse;
import com.interswitch.smartmoveserver.model.response.GetDeviceIdResponse;
import com.interswitch.smartmoveserver.model.response.GetOwnerIdResponse;
import com.interswitch.smartmoveserver.repository.BusRepository;
import com.interswitch.smartmoveserver.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private BusRepository busRepository;

    public DeviceConnectionResponse connectDevice(DeviceConnection deviceConnection) {
        Device device = new Device();
        device.setOwnerId(deviceConnection.getOwnerId());
        device.setDeviceId(deviceConnection.getDeviceId());
        device.setStatus(deviceConnection.getStatus());
        device.setVersionEmv(deviceConnection.getVersionEmv());
        device.setVersionHardware(deviceConnection.getVersionHardware());
        device.setVersionSoftware(deviceConnection.getVersionSoftware());
        device.setFareType(deviceConnection.getFareType());
        device.setPeriodGps(deviceConnection.getPeriodGps());
        device.setPeriodTransactionUpload(deviceConnection.getPeriodTransactionUpload());
        deviceRepository.save(device);
        DeviceConnectionResponse deviceConnectionResponse = new DeviceConnectionResponse();
        deviceConnectionResponse.setMessageId(deviceConnection.getMessageId());
        deviceConnectionResponse.setTimeDate(LocalDateTime.now().toString());
        deviceConnectionResponse.setResponseCode("00");
        return deviceConnectionResponse;
    }

    public GetOwnerIdResponse getOwnerId(GetOwnerId getOwnerId){
        GetOwnerIdResponse getOwnerIdResponse = new GetOwnerIdResponse();
        busRepository.findById(getOwnerId.getDeviceId()).ifPresent(bus -> {
            getOwnerIdResponse.setOwnerId(bus.getId());
        });
        getOwnerIdResponse.setMessageId(getOwnerId.getMessageId());
        getOwnerIdResponse.setResponseCode("00");
        return getOwnerIdResponse;
    }
    public GetDeviceIdResponse getDeviceId(GetDeviceId getDeviceId){
        GetDeviceIdResponse getDeviceIdResponse = new GetDeviceIdResponse();
        getDeviceIdResponse.setDeviceId(UUID.randomUUID().toString());
        getDeviceIdResponse.setMessageId(getDeviceId.getMessageId());
        getDeviceIdResponse.setResponseCode("00");
        return getDeviceIdResponse;
    }
}
