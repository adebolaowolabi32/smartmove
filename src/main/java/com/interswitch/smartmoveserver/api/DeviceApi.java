package com.interswitch.smartmoveserver.api;

import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.request.DeviceConnection;
import com.interswitch.smartmoveserver.model.request.GetDeviceId;
import com.interswitch.smartmoveserver.model.response.DeviceConnectionResponse;
import com.interswitch.smartmoveserver.model.response.GetDeviceIdResponse;
import com.interswitch.smartmoveserver.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author adebola.owolabi
 */
@RestController
@RequestMapping("/devices")
public class DeviceApi {

    @Autowired
    private DeviceService deviceService;

    @PostMapping(value = "/connect", consumes = "application/json", produces = "application/json")
    public DeviceConnectionResponse connectDevice(@RequestBody @Validated DeviceConnection deviceConnection) {
        return deviceService.connectDevice(deviceConnection);
    }

/*
    @PostMapping(value = "/getOwnerId", consumes = "application/json", produces = "application/json")
    public GetOwnerIdResponse getOwnerId(@RequestBody GetOwnerId getOwnerId) {
        return deviceService.getOwnerId(getOwnerId);
    }
*/

    @GetMapping(produces = "application/json")
    private List<Device> getAll() {
        return deviceService.getAll();
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private Device save(@Validated @RequestBody Device device) {
        return deviceService.save(device);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    private Device findById(@Validated @PathVariable long id) {
        return deviceService.findById(id);
    }

    @GetMapping(value = "/findByOwner/{owner}", produces = "application/json")
    private List<Device> findAllByOwner(@Validated @PathVariable long owner) {
        return deviceService.findAllByOwner(owner);
    }

    @GetMapping(value = "/findByType/{type}", produces = "application/json")
    private List<Device> find(@Validated @PathVariable Enum.DeviceType type) {
        return deviceService.find(type);
    }

    @PostMapping(value = "/{deviceId}/assignToVehicle/{vehicleId}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void assignToVehicle(@Validated @PathVariable long deviceId, @Validated @PathVariable long vehicleId) {
        deviceService.assignToVehicle(deviceId, vehicleId);
    }

    @PostMapping(value = "/{deviceId}/assignToAgent/{agentId}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void assignToAgent(@Validated @PathVariable long deviceId, @Validated @PathVariable long agentId) {
        deviceService.assignToAgent(deviceId, agentId);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    private Device update(@Validated @RequestBody Device device) {
        return deviceService.update(device);
    }

    @DeleteMapping("/{id}")
    private void delete(@Validated @PathVariable long id) {
        deviceService.delete(id);
    }

    @PostMapping(value = "/getDeviceId", consumes = "application/json", produces = "application/json")
    public GetDeviceIdResponse getDeviceId(@RequestBody GetDeviceId getDeviceId) {
        return deviceService.getDeviceId(getDeviceId);
    }

    @PostMapping(value = "/activate/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void activate(@Validated @PathVariable long id) {
        deviceService.activate(id);
    }

    @PostMapping(value = "/deactivate/{id}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    private void deactivate(@Validated @PathVariable long id) {
        deviceService.deactivate(id);
    }

}
