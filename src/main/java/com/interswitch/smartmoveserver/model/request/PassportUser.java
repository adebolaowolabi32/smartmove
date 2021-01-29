package com.interswitch.smartmoveserver.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PassportUser {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String mobileNo;

    private String email;

    private boolean enabled;

    private String title;

    private boolean firstLogin;
}