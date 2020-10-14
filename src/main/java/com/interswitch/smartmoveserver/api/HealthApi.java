package com.interswitch.smartmoveserver.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * @author adebola.owolabi
 */
@Slf4j
@RestController
@RequestMapping("/health")
public class HealthApi {

    @GetMapping()
    public String getHealth() {
        return "I'm alive!";
    }
}
