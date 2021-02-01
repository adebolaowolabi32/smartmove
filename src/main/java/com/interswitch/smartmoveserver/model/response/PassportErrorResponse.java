package com.interswitch.smartmoveserver.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PassportErrorResponse {
    @JsonProperty("error")
    private String error;
    @JsonProperty("error_description")
    private String errorDescription;
    @JsonProperty("error_code")
    private String errorCode;
    @JsonProperty("code")
    private String code;
    @JsonProperty("description")
    private String description;
}
