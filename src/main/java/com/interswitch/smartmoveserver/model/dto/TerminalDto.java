package com.interswitch.smartmoveserver.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TerminalDto {

    @JsonProperty("TerminalName")
    private String terminalName;

    @JsonProperty("TransportMode")
    private String transportMode;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("State")
    private String state;

    @JsonProperty("LGA")
    private String lga;
}
