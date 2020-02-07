package com.interswitch.smartmoveserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.model.request.DeviceConnection;
import com.interswitch.smartmoveserver.model.request.GetDeviceId;
import com.interswitch.smartmoveserver.model.request.GetOwnerId;
import com.interswitch.smartmoveserver.model.response.DeviceConnectionResponse;
import com.interswitch.smartmoveserver.model.response.GetDeviceIdResponse;
import com.interswitch.smartmoveserver.model.response.GetOwnerIdResponse;
import com.interswitch.smartmoveserver.service.DeviceService;
import com.interswitch.smartmoveserver.controller.DeviceController;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = DeviceController.class)
public class DeviceControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private DeviceService deviceService;

    private GetDeviceId getDeviceId;
    private GetDeviceIdResponse getDeviceIdResponse;
    private GetOwnerId getOwnerId;
    private GetOwnerIdResponse getOwnerIdResponse;
    private DeviceConnection deviceConnection;
    private DeviceConnectionResponse deviceConnectionResponse;


    @Before
    public void setup() {
        deviceConnection = new DeviceConnection();
        deviceConnection.setMessageId("id_message");
        deviceConnection.setDeviceId("id_device");
        deviceConnectionResponse = new DeviceConnectionResponse();
        deviceConnectionResponse.setResponseCode("00");
        deviceConnectionResponse.setMessageId("id_message");
        getOwnerId = new GetOwnerId();
        getOwnerId.setDeviceId("id_device");
        getOwnerId.setMessageId("id_message");
        getOwnerIdResponse = new GetOwnerIdResponse();
        getOwnerIdResponse.setResponseCode("00");
        getOwnerIdResponse.setMessageId("id_message");
        getDeviceId = new GetDeviceId();
        getDeviceId.setMessageId("id_message");
        getDeviceIdResponse = new GetDeviceIdResponse();
        getDeviceIdResponse.setResponseCode("00");
        getDeviceIdResponse.setMessageId("id_message");
    }

    @Test
    public void testConnectDevice() throws Exception {
        when(deviceService.connectDevice(deviceConnection)).thenReturn(deviceConnectionResponse);

        mvc.perform(post("/device/connect")
                .content(new ObjectMapper().writeValueAsString(deviceConnection))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(deviceConnectionResponse)));
    }

    @Test
    public void testGetOwnerID() throws Exception {
       when(deviceService.getOwnerId(getOwnerId)).thenReturn(getOwnerIdResponse);
        mvc.perform(post("/device/getOwnerId")
                .content(new ObjectMapper().writeValueAsString(getOwnerId))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(getOwnerIdResponse)));

    }

    @Test
    public void testGetDeviceID() throws Exception {
       when(deviceService.getDeviceId(getDeviceId)).thenReturn(getDeviceIdResponse);
        mvc.perform(post("/device/getDeviceId")
                .content(new ObjectMapper().writeValueAsString(getDeviceId))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(getDeviceIdResponse)));

    }
}
