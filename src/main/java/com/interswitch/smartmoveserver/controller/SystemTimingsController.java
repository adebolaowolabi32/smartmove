package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.SystemTimings;
import com.interswitch.smartmoveserver.model.request.GetSystemTimings;
import com.interswitch.smartmoveserver.model.response.GetSystemTimingsResponse;
import com.interswitch.smartmoveserver.service.SystemTimingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device")
public class SystemTimingsController {
    @Autowired
    private SystemTimingsService systemTimingsService;

    @PostMapping(value = "/systemTimings", consumes = "application/json", produces = "application/json")
    public SystemTimings saveTimings(@RequestBody @Validated SystemTimings systemTimings) {
        return systemTimingsService.saveSystemTimings(systemTimings);
    }

    @PostMapping(value = "/getSystemTimings", consumes = "application/json", produces = "application/json")
    public GetSystemTimingsResponse getTimings(@RequestBody @Validated GetSystemTimings getSystemTimings) {
        return systemTimingsService.getSystemTimings(getSystemTimings);
    }
}
