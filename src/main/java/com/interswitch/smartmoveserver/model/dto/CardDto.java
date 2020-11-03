package com.interswitch.smartmoveserver.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CardDto {

    @JsonProperty("Pan")
    private String pan;
    //yyyy-mm-dd
    @JsonProperty("Expiry")
    private String expiry;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Enabled")
    private String enabled;
}
