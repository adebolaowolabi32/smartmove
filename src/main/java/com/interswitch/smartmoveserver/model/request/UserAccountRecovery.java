package com.interswitch.smartmoveserver.model.request;

import lombok.Data;

@Data
public class UserAccountRecovery {
    private String uuid;
    private String otp;
    private String password;
    private String confirmPassword;
}
