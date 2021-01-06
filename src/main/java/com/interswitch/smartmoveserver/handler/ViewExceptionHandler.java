package com.interswitch.smartmoveserver.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice("com.interswitch.smartmoveserver.controller")
@Slf4j
public class ViewExceptionHandler {

    public static final String ERROR_MESSAGE_TEMPLATE = "message: %s %n request uri: %s";
    public static final String FIELD_ERROR_SEPARATOR = ": ";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<String> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + FIELD_ERROR_SEPARATOR + error.getDefaultMessage())
                .collect(Collectors.toList());
        redirectAttributes.addFlashAttribute("error", validationErrors.get(0));
        return "redirect:" + request.getServletPath() + "?" + request.getQueryString();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                                  HttpHeaders headers, HttpStatus status, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", exception.getLocalizedMessage());
        return "redirect:" + request.getServletPath() + "?" + request.getQueryString();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataViolation(DataIntegrityViolationException exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        final String validationError = exception.getCause().getCause().getLocalizedMessage();
        String message = "";
        if (validationError.contains("duplicate")) {
            message = "Cannot save duplicate value. The duplicate value is " + StringUtils.substringBetween(validationError, "(", ")") + ".";
        } else if (validationError.contains("delete statement conflicted with reference"))
            message = "Cannot delete this entity because it is tied to one or more " + StringUtils.substringBetween(validationError, "dbo.", "\"") + ".";
        else message = validationError;
        redirectAttributes.addFlashAttribute("error", message);
        return "redirect:" + request.getServletPath() + "?" + request.getQueryString();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolation(ConstraintViolationException exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        final List<String> validationErrors = exception.getConstraintViolations().stream().
                map(violation ->
                        violation.getPropertyPath() + FIELD_ERROR_SEPARATOR + violation.getMessage())
                .collect(Collectors.toList());
        redirectAttributes.addFlashAttribute("error", validationErrors.get(0));
        return "redirect:" + request.getServletPath() + "?" + request.getQueryString();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponsestatusExceptions(ResponseStatusException exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        final HttpStatus status = exception.getStatus();
        final String localizedMessage = exception.getLocalizedMessage();
        final String path = request.getServletPath();
        String message = "";
        if (localizedMessage.contains("{")) {
            message = (StringUtils.isNotEmpty(localizedMessage) ?
                    StringUtils.substringBetween(localizedMessage, "{", "}") : status.getReasonPhrase());
        } else {
            message = (StringUtils.isNotEmpty(localizedMessage) ?
                    StringUtils.substringBetween(localizedMessage, "\"", "\"") : status.getReasonPhrase());
        }
        log.error(String.format(ERROR_MESSAGE_TEMPLATE, message, path), exception);
        redirectAttributes.addFlashAttribute("error", message);
        return "redirect:" + path + "?" + request.getQueryString();
    }

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);
        final HttpStatus status = responseStatus!=null ? responseStatus.value():HttpStatus.INTERNAL_SERVER_ERROR;
        final String localizedMessage = exception.getLocalizedMessage();
        final String path = request.getServletPath();
        String message = (StringUtils.isNotEmpty(localizedMessage) ? localizedMessage:status.getReasonPhrase());
        log.error(String.format(ERROR_MESSAGE_TEMPLATE, message, path), exception);
        redirectAttributes.addFlashAttribute("error", message);
        return "redirect:" + path + "?" + request.getQueryString();
    }
}
