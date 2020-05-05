package com.interswitch.smartmoveserver.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ViewExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResponseStatusException.class})
    public ModelAndView handleException(ResponseStatusException ex, HttpServletRequest request) {
        String targetView;
        /*if (handlerMethod != null && handlerMethod.hasMethodAnnotation(ExceptionTargetView.class)) {
            targetView = handlerMethod.getMethodAnnotation(ExceptionTargetView.class).value();
        } else {
            targetView = "error";
        }*/
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(request.getServletPath());
        modelAndView.addObject("status", ex.getStatus());
        modelAndView.addObject("error", ex.getMessage());
        return modelAndView;
    }
}