package com.interswitch.smartmoveserver.model.dto;


import lombok.Data;

@Data
public class TerminalDto {

    private String TerminalName;

    private String TransportMode;

    private String Country;

    private String State;

    private String Lga;

    private boolean enabled;
}
