package com.interswitch.smartmoveserver.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDto {

    @JsonProperty("Password")
    private String password;

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("LastName")
    private String lastName;

    @JsonProperty("MobileNo")
    private String mobileNo;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Role")
    private String role;

    @JsonProperty("Enabled")
    private String enabled;

}
