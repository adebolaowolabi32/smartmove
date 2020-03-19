package com.interswitch.smartmoveserver.model.request;

import lombok.Data;

@Data
public class PassportUser {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String mobileNo;

    private String email;

    private boolean enabled;
}