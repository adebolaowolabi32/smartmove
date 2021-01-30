package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.response.ErrorResponse;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;

import javax.net.ssl.SSLException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

@Component
public class ErrorResponseService {

    public ErrorResponse fromException(Throwable e, WebRequest request, int httpStatus, String errorMessage) {
        HttpMethod httpMethod = ((ServletWebRequest) request).getHttpMethod();
        String message = errorMessage;
        String eMessage = e.getMessage();
        ErrorResponse response = new ErrorResponse();
        int code = httpStatus;
        ArrayList<String> errors = new ArrayList<>();
        if (e instanceof MethodArgumentNotValidException) {
            code = 400;
            ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                errors.add(error.getDefaultMessage());
            });

            message = e.getMessage();
        }
        if (e instanceof MissingServletRequestParameterException) {
            code = 400;
            message = ((MissingServletRequestParameterException) e).getParameterName() + " parameter is missing";
        }
        if (e instanceof ConstraintViolationException) {
            code = 400;
            for (ConstraintViolation<?> violation : ((ConstraintViolationException) e).getConstraintViolations()) {
                errors.add(violation.getRootBeanClass().getName() + " " +
                        violation.getPropertyPath() + ": " + violation.getMessage());
            }
            message = e.getLocalizedMessage();
        }
        if (e instanceof MethodArgumentTypeMismatchException) {
            code = 400;
            message = ((MethodArgumentTypeMismatchException) e).getName() + " should be of type " + ((MethodArgumentTypeMismatchException) e).getRequiredType().getName();
        }
        if (e instanceof ResponseStatusException) {
            code = ((ResponseStatusException) e).getStatus().value();
            String key = StringUtils.substringBetween(eMessage, "\"", "\"");
            message = (key != "") ? key : message;
        }

        if (e instanceof SocketException || e instanceof SSLException || e instanceof SQLServerException || e instanceof RestClientException) {
            code = 503;
            message = "Either remote server cannot be reached or network connection was reset/broken. Please try again later";
        }
        if (e instanceof UnknownHostException) {
            message = "Unknown Host. Host Ip could not be determined";
        }
        if (e instanceof HttpServerErrorException.InternalServerError) {
            code = 500;
            message = "An Unexpected error has occurred. Please try again later";
        }
        if (e instanceof MethodNotAllowedException) {
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