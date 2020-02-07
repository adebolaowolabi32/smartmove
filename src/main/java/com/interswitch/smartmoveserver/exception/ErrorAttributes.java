package com.interswitch.smartmoveserver.exception;

import com.interswitch.smartmoveserver.model.response.ErrorResponse;
import com.interswitch.smartmoveserver.service.ErrorResponseService;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Component
public class ErrorAttributes extends DefaultErrorAttributes {

    private ErrorResponseService errorResponseService;

    public ErrorAttributes(ErrorResponseService errorResponseService) {
        this.errorResponseService = errorResponseService;
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest request,
                                                  boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);
        Throwable error = getError(request);
        ErrorResponse response = new ErrorResponse();
        if (Objects.nonNull(error)) {
            int status = Integer.parseInt(errorAttributes.get("status").toString());
            String errorMessage = (errorAttributes.get("message") != null) ? errorAttributes.get("message").toString() : HttpStatus.valueOf(status).toString();
            response = errorResponseService.fromException(error, status, errorMessage);
            errorAttributes.clear();
        }
        request.getParameterNames();
        errorAttributes.put("timestamp", new Date());
        errorAttributes.put("status", response.getStatus());
        errorAttributes.put("message", response.getMessage());
        if (Objects.nonNull(response.getErrors()) && !response.getErrors().isEmpty()) {
            errorAttributes.put("errors", response.getErrors());
        }

        return errorAttributes;
    }

}
