package com.interswitch.smartmoveserver.model.response;
import lombok.Data;

@Data
public class UserRoleResponse {
    private long id;
    private String email;
    private String role;
    private long ownerId;
}
