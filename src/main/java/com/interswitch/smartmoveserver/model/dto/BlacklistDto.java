package com.interswitch.smartmoveserver.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BlacklistDto {

    @JsonProperty("Identifier")
    private String identifier;

    @JsonProperty("ItemType")
    private String itemType;
}

