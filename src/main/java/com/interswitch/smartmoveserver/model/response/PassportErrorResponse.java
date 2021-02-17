package com.interswitch.smartmoveserver.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

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
    @JsonProperty("errors")
    private List<Error> errors;

    @Data
    public static class Error {
        String fieldName;
        String message;
    }
}
