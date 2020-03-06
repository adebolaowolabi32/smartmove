package com.interswitch.smartmoveserver.model.request;

import com.interswitch.smartmoveserver.model.Enum;
import lombok.Data;

@Data
public class OnboardUser {
    private Enum.Role type;
    private String emailAddress;
}
