package com.interswitch.smartmoveserver.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.interswitch.smartmoveserver.model.Manifest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
public class ManifestDto implements Serializable {

    @JsonProperty
    private String SeatNo;

    @JsonProperty
    private String Name;

    @JsonProperty
    private String Address;

    @JsonProperty
    private String Gender;

    @JsonProperty
    private String Bvn;

    @JsonProperty
    private String Nationality;

    @JsonProperty
    private String IdCategory;

    @JsonProperty
    private String IdNumber;

    @JsonProperty
    private String ContactMobile;

    @JsonProperty
    private String ContactEmail;

    @JsonProperty
    private String NextOfKinName;

    @JsonProperty
    private String NextOfKinMobile;

    @JsonProperty
    private String Boarded;

    @JsonProperty
    private String Completed;

    @JsonProperty
    private String TimeOfBoarding;

    @JsonProperty
    private String TimeOfCompletion;
}
