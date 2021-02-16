package com.interswitch.smartmoveserver.model.request;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserAccountRecoveryNotification {

    @NotBlank(message="please enter your username")
    private String username;
    private String destinationUri;
}
