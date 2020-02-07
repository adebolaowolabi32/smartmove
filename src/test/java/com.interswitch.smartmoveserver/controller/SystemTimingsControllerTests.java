package com.interswitch.smartmoveserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.model.SystemTimings;
import com.interswitch.smartmoveserver.model.request.GetSystemTimings;
import com.interswitch.smartmoveserver.model.response.GetSystemTimingsResponse;
import com.interswitch.smartmoveserver.service.SystemTimingsService;
import com.interswitch.smartmoveserver.controller.SystemTimingsController;
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
@ContextConfiguration(classes = SystemTimingsController.class)
public class SystemTimingsControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private SystemTimingsService systemTimingsService;

    private SystemTimings systemTimings;
    private GetSystemTimings getTimings;
    private GetSystemTimingsResponse getTimingsResponse;

    @Before
    public void setup() {
        systemTimings = new SystemTimings();
        systemTimings.setPeriodGPS("200");
        systemTimings.setPeriodGPS("120");
        systemTimings.setPeriodTransactionUpload("15");
        getTimings = new GetSystemTimings();
        getTimings.setDeviceId("id_device");
        getTimings.setMessageId("id_message");
        getTimingsResponse = new GetSystemTimingsResponse();
        getTimingsResponse.setMessageId("id_message");
        getTimingsResponse.setResponseCode("00");
    }

    @Test
    public void testSaveTimings() throws Exception {
       when(systemTimingsService.saveSystemTimings(systemTimings)).thenReturn(systemTimings);

        mvc.perform(post("/device/systemTimings")
                .content(new ObjectMapper().writeValueAsString(systemTimings))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(systemTimings)));
    }

    @Test
    public void testGetTimings() throws Exception {
       when(systemTimingsService.getSystemTimings(getTimings)).thenReturn(getTimingsResponse);

        mvc.perform(post("/device/getSystemTimings")
                .content(new ObjectMapper().writeValueAsString(getTimings))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(getTimingsResponse)));
    }
}
