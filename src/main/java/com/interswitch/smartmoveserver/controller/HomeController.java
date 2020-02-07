package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.model.request.DeviceConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/health")
public class HomeController {

    @GetMapping()
    public String getHealth() {
        return "I'm alive!";
    }

    @PostMapping()
    public String sayHello(@RequestBody DeviceConnection deviceConnection) {
        log.info("Message received from device {}", deviceConnection);
        return "Device Connected Successfully";
    }
}
