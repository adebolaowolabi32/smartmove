package com.interswitch.smartmoveserver.controller;

import com.interswitch.smartmoveserver.controller.BusController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = BusController.class)
public class BusControllerTests {

    @Test
    public void runTests(){

    }
}
