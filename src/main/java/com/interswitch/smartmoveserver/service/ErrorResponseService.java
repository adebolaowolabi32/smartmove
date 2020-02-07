package com.interswitch.smartmoveserver.service;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.interswitch.smartmoveserver.model.response.ErrorResponse;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import javax.net.ssl.SSLException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ErrorResponseService {

    public ErrorResponse fromException(Throwable e, int status, String errorMessage) {
        String message = errorMessage;
        String eMessage = e.getMessage();
        ErrorResponse response = new ErrorResponse();
        int code = status;
        ArrayList<FieldError> errors = new ArrayList<>();
        if (e instanceof DuplicateKeyException) {
            code = 409;
            String[] keys = StringUtils.substringsBetween(eMessage, "\"", "\"");
            String key = " already exists";
            message = (keys != null) ? keys[0] + key : message;
        }
        if (e instanceof ServerWebInputException || e instanceof MismatchedInputException) {
            code = 400;
            if (e instanceof WebExchangeBindException) {
                List<FieldError> fieldErrors = ((WebExchangeBindException) e).getFieldErrors();
                message = ((WebExchangeBindException) e).getReason();
                errors.addAll(fieldErrors);
            } else {
                String[] keys = StringUtils.substringsBetween(eMessage, "(", ")");
                String[] key = (keys != null) ? StringUtils.substringsBetween(keys[0], "\"", "\"") : StringUtils.substringsBetween(eMessage, "\"", "\"");
                String field = "Bad Input";
                if (key != null)
                    field = (key[0].contains("mismatch")) ? "Data Type mismatch for value " + key[1] : "Invalid value for field " + key[0];
                message = "Validation failure: " + field;
            }
        }
        if (e instanceof HttpClientErrorException.NotFound) {
            code = 404;
            String[] key = (eMessage != null) ? StringUtils.substringsBetween(eMessage, "\"", "\"") : new String[]{};
            message = (key[0] != null) ? key[0] : "Not Found";
        }

        if (e instanceof SocketException || e instanceof SSLException || e instanceof SQLServerException || e instanceof HttpClientErrorException) {
            code = 503;
            message = "Either remote server cannot be reached or network connection was reset/broken. Please try again later";
        }
        if (e instanceof ResourceAccessException) {
            message = "Something went wrong, please bear with us while we fix it";
        }
        if (e instanceof UnknownHostException) {
            message = "Unknown Host. Host Ip could not be determined";
        }
        if (e instanceof HttpServerErrorException.InternalServerError || e instanceof ServerErrorException) {
            code = 500;
            message = "An unexpected server error has occurred. Please try again later";
        }
        if (e instanceof MethodNotAllowedException) {
            code = 405;
        }
        if (e instanceof UnsupportedMediaTypeStatusException) {
            code = 415;
        }
        if (e instanceof IllegalArgumentException) {
            code = 400;
        }
        response.setStatus(code);
        response.setMessage(message);
        response.setErrors(errors);
        return response;
    }
}
