package com.interswitch.smartmoveserver.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.model.Config;
import com.interswitch.smartmoveserver.service.ConfigService;
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

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = ConfigApi.class)
public class ConfigApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ConfigService configService;

    private Config config;

    @BeforeAll
    public void setup() {
        config = new Config();
        long id = 1000002;
        config.setId(id);
        config.setValue("300");
    }

    @Test
    public void testSave() throws Exception {
       when(configService.save(config)).thenReturn(config);
        mvc.perform(post("/api/configurations")
                .content(new ObjectMapper().writeValueAsString(config))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testUpdate() throws Exception {
        when(configService.update(config)).thenReturn(config);
        mvc.perform(put("/api/configurations")
                .content(new ObjectMapper().writeValueAsString(config))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testfindAll() throws Exception {
        when(configService.findAll()).thenReturn(Arrays.asList(config, new Config()));
        mvc.perform(get("/api/configurations")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetFindById() throws Exception {
        when(configService.findById(config.getId())).thenReturn(config);
        mvc.perform(get("/api/configurations/{id}", config.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    /*@Test
    public void testGetFindByName() throws Exception {
        when(configService.findByName(Enum.ConfigList.TRANSACTION_UPLOAD_PERIOD)).thenReturn(config);
        mvc.perform(get("/api/configurations/{name}", config.getName())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(config)));
    }*/

    @Test
    public void testDelete() throws Exception {
        mvc.perform(delete("/api/configurations/{id}", config.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
