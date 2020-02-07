package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.request.DeviceConnection;
import com.interswitch.smartmoveserver.model.request.GetDeviceId;
import com.interswitch.smartmoveserver.model.request.GetOwnerId;
import com.interswitch.smartmoveserver.model.response.DeviceConnectionResponse;
import com.interswitch.smartmoveserver.model.response.GetDeviceIdResponse;
import com.interswitch.smartmoveserver.model.response.GetOwnerIdResponse;
import com.interswitch.smartmoveserver.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping(value = "/connect", consumes = "application/json", produces = "application/json")
    public DeviceConnectionResponse connectDevice(@RequestBody @Validated DeviceConnection deviceConnection) {
        return deviceService.connectDevice(deviceConnection);
    }

    @PostMapping(value = "/getOwnerId", consumes = "application/json", produces = "application/json")
    public GetOwnerIdResponse getOwnerId(@RequestBody GetOwnerId getOwnerId) {
        return deviceService.getOwnerId(getOwnerId);
    }

    @PostMapping(value = "/getDeviceId", consumes = "application/json", produces = "application/json")
    public GetDeviceIdResponse getDeviceId(@RequestBody GetDeviceId getDeviceId) {
        return deviceService.getDeviceId(getDeviceId);
    }
/*
    @PutMapping(value = "/activate", consumes = "application/json", produces = "application/json")
    public Object activateDevice(@RequestBody GetDeviceId getDeviceId) {

        return deviceService.activateDevice(getDeviceId);
    }

    @PutMapping(value = "/deactivate", consumes = "application/json", produces = "application/json")
    public Object deactivateDevice(@RequestBody GetDeviceId getDeviceId) {

        return deviceService.deactivateDevice(getDeviceId);
    }*/

}
