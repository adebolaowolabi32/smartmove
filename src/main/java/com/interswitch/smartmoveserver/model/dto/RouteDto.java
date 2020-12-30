package com.interswitch.smartmoveserver.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RouteDto {

    @JsonProperty("TransportMode")
    private String  transportMode;

    @JsonProperty("StartTerminalId")
    private long startTerminalId;

    @JsonProperty("StopTerminalId")
    private long stopTerminalId;

    @JsonProperty("Fare")
    private long fare;

    @JsonProperty("Enabled")
    private boolean enabled;
}
