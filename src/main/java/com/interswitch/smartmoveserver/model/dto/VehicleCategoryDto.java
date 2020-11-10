package com.interswitch.smartmoveserver.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VehicleCategoryDto {

    @JsonProperty("TransportMode")
    private String transportMode;

    @JsonProperty("Name")
    private String name;

    //use this to get VehicleMake object by name
    @JsonProperty("VehicleMakeName")
    private String vehicleMakeName;

    //use this to get vehicle model object by name
    @JsonProperty("ModelName")
    private String modelName;

    @JsonProperty("Color")
    private String color;

    @JsonProperty("PictureUrl")
    private String pictureUrl;

    @JsonProperty("NumberOfRows")
    private int numberOfRows;

    @JsonProperty("NumberOfColumns")
    private int numberOfColumns;

    @JsonProperty("Capacity")
    private int capacity;
}
