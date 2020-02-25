package com.interswitch.smartmoveserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.request.DeviceConnection;
import com.interswitch.smartmoveserver.model.request.GetDeviceId;
import com.interswitch.smartmoveserver.model.request.GetOwnerId;
import com.interswitch.smartmoveserver.model.response.DeviceConnectionResponse;
import com.interswitch.smartmoveserver.model.response.GetDeviceIdResponse;
import com.interswitch.smartmoveserver.model.response.GetOwnerIdResponse;
import com.interswitch.smartmoveserver.service.DeviceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = DeviceController.class)
public class DeviceControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private DeviceService deviceService;

    private Device device;

    private GetDeviceId getDeviceId;
    private GetDeviceIdResponse getDeviceIdResponse;
    private GetOwnerId getOwnerId;
    private GetOwnerIdResponse getOwnerIdResponse;
    private DeviceConnection deviceConnection;
    private DeviceConnectionResponse deviceConnectionResponse;

    @Before
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
        deviceConnection = new DeviceConnection();
        deviceConnection.setMessageId("id_message");
        deviceConnection.setDeviceId("id_device");
        deviceConnectionResponse = new DeviceConnectionResponse();
        deviceConnectionResponse.setResponseCode("00");
        deviceConnectionResponse.setMessageId("id_message");
    }

    @Test
    public void testSave() throws Exception {
        when(deviceService.save(device)).thenReturn(device);
        mvc.perform(post("/devices")
                .content(new ObjectMapper().writeValueAsString(device))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testUpdate() throws Exception {
        when(deviceService.update(device)).thenReturn(device);
        mvc.perform(put("/devices")
                .content(new ObjectMapper().writeValueAsString(device))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testGetAll() throws Exception {
        when(deviceService.getAll()).thenReturn(Arrays.asList(device, new Device()));
        mvc.perform(get("/devices")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void testGetFindById() throws Exception {
        when(deviceService.findById(device.getId())).thenReturn(device);
        mvc.perform(get("/devices/{id}", device.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(delete("/devices/{id}", device.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testConnectDevice() throws Exception {
        when(deviceService.connectDevice(deviceConnection)).thenReturn(deviceConnectionResponse);

        mvc.perform(post("/devices/connect")
                .content(new ObjectMapper().writeValueAsString(deviceConnection))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(deviceConnectionResponse)));
    }
    
}
