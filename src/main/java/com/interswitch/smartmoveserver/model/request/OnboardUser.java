package com.interswitch.smartmoveserver.model.request;

import com.interswitch.smartmoveserver.model.User;
import lombok.Data;

@Data
public class OnboardUser {
    private User.UserType type;
    private String emailAddress;
}
