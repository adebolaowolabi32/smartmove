package com.interswitch.smartmoveserver.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordResetRequest {
    @NotBlank(message="please enter your username")
    private String username;
}
