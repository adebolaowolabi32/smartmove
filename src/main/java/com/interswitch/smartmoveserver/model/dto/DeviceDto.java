package com.interswitch.smartmoveserver.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class DeviceDto {

    //name is unique and required
    @JsonProperty("Name")
    private String name;

    //unique and required
    @JsonProperty("DeviceId")
    private String deviceId;

    @JsonProperty("DeviceType")
    private String deviceType;

    @JsonProperty("HardwareVersion")
    private String hardwareVersion;

    @JsonProperty("SoftwareVersion")
    private String softwareVersion;

    @JsonProperty("BatteryPercentage")
    private int batteryPercentage;

    @JsonProperty("FareType")
    private String fareType;

    @JsonProperty("DeviceStatus")
    private String deviceStatus;

    @JsonProperty("Enabled")
    private String enabled;
}
