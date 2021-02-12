package com.interswitch.smartmoveserver.model.request;

import lombok.Data;

@Data
public class ChangePassword {
    private String oldPassword;
    private String password;
    private String confirmPassword;
}
