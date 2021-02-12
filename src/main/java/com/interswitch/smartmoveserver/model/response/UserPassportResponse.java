package com.interswitch.smartmoveserver.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class UserPassportResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonIgnore
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonIgnore
    private String scope;
    private String user_name;
    private String firstName;
    private String lastName;
    @JsonIgnore
    private boolean emailVerified;
    @JsonIgnore
    private boolean firstLogin;
    private String mobileNo;
    @JsonIgnore
    private boolean mobileNoVerified;
    @JsonIgnore
    @JsonProperty("client_name")
    private String clientName;
    private String email;
    @JsonIgnore
    private String passportID;
    @JsonIgnore
    private UUID jti;
    private String role;
}
