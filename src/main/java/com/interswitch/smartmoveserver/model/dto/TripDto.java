package com.interswitch.smartmoveserver.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TripDto implements Serializable {

    @JsonProperty
    private String TransportMode;
    @JsonProperty
    private String VehicleNumber;
    @JsonProperty
    private long  ScheduleId;
    @JsonProperty
    private String DriverEmail;
}
