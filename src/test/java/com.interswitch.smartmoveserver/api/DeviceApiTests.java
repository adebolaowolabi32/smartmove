package com.interswitch.smartmoveserver.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.DeviceService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = DeviceApi.class)
public class DeviceApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private DeviceService deviceService;

    private Device device;

    @BeforeAll
    public void setup() {
        device = new Device();
        long id = 10000023;
        device.setId(id);
        device.setDeviceId("012345");
        device.setOwner(new User());
        device.setType(Enum.DeviceType.READER);
        device.setDeviceStatus(Enum.DeviceStatus.DISCONNECTED);
        device.setHardwareVersion("1.0.0");
        device.setSoftwareVersion("2.0.2");
        device.setFareType(Enum.FareType.FIXED);
    }

    @Test
    public void testSave() throws Exception {
        when(deviceService.save(device)).thenReturn(device);
        mvc.perform(post("/api/devices")
                .content(new ObjectMapper().writeValueAsString(device))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testUpdate() throws Exception {
        when(deviceService.update(device, any(Principal.class))).thenReturn(device);
        mvc.perform(put("/api/devices")
                .content(new ObjectMapper().writeValueAsString(device))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testfindAll() throws Exception {
        when(deviceService.findAll()).thenReturn(Arrays.asList(device, new Device()));
        mvc.perform(get("/api/devices")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void testGetFindById() throws Exception {
        when(deviceService.findById(device.getId(), any(Principal.class))).thenReturn(device);
        mvc.perform(get("/api/devices/{id}", device.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(delete("/api/devices/{id}", device.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
