package com.interswitch.smartmoveserver.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ManifestDto implements Serializable {

    @JsonProperty("SeatNo")
    private String seatNo;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("Bvn")
    private String bvn;

    @JsonProperty("Nationality")
    private String nationality;

    @JsonProperty("IdCategory")
    private String idCategory;

    @JsonProperty("IdNumber")
    private String idNumber;

    @JsonProperty("ContactMobile")
    private String contactMobile;

    @JsonProperty("ContactEmail")
    private String contactEmail;

    @JsonProperty("NextOfKinName")
    private String nextOfKinName;

    @JsonProperty("NextOfKinMobile")
    private String nextOfKinMobile;

    @JsonProperty("Boarded")
    private String boarded;

    @JsonProperty("Completed")
    private String completed;

    @JsonProperty("TimeOfBoarding")
    private String timeOfBoarding;

    @JsonProperty("TimeOfBoarding")
    private String timeOfCompletion;
}
