package com.interswitch.smartmoveserver.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RouteDto {

    @JsonProperty("TransportMode")
    private String  transportMode;

    @JsonProperty("StartTerminalName")
    private String startTerminalName;

    @JsonProperty("StopTerminalName")
    private String stopTerminalName;

    @JsonProperty("Price")
    private long price;

    @JsonProperty("Enabled")
    private boolean enabled;
}
