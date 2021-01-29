package com.interswitch.smartmoveserver.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TripDto implements Serializable {

    @JsonProperty("TransportMode")
    private String transportMode;
    @JsonProperty("VehicleNumber")
    private String vehicleNumber;
    @JsonProperty("ScheduleId")
    private long scheduleId;
    @JsonProperty("DriverEmail")
    private String driverEmail;
}
