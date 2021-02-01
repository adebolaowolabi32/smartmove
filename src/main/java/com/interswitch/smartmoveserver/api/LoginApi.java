package com.interswitch.smartmoveserver.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.interswitch.smartmoveserver.model.request.UserLoginRequest;
import com.interswitch.smartmoveserver.model.response.UserPassportResponse;
import com.interswitch.smartmoveserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class LoginApi {

    @Autowired
    private UserService userService;

    @PostMapping(value="/login",produces = "application/json", consumes = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    private UserPassportResponse login(@Validated @RequestBody UserLoginRequest loginRequest) throws JsonProcessingException {
        log.info("Calling login===>");
        return userService.doUserAuthFromApi(loginRequest);
    }
}
