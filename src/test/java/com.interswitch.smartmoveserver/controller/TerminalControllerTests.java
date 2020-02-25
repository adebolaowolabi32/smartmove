package com.interswitch.smartmoveserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Terminal;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.TerminalService;
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
@ContextConfiguration(classes = TerminalController.class)
public class TerminalControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TerminalService terminalService;

    private Terminal terminal;

    @Before
    public void setup() {
        terminal = new Terminal();
        long id = 10000023;
        terminal.setId(id);
        terminal.setName("my_terminal");
        terminal.setType(Enum.TransportMode.RAIL);
        terminal.setOwner(new User());
        terminal.setLocation("my_location_one");
        terminal.setActive(true);
    }

    @Test
    public void testSave() throws Exception {
        when(terminalService.save(terminal)).thenReturn(terminal);
        mvc.perform(post("/terminals")
                .content(new ObjectMapper().writeValueAsString(terminal))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testUpdate() throws Exception {
        when(terminalService.update(terminal)).thenReturn(terminal);
        mvc.perform(put("/terminals")
                .content(new ObjectMapper().writeValueAsString(terminal))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testGetAll() throws Exception {
        when(terminalService.getAll()).thenReturn(Arrays.asList(terminal, new Terminal()));
        mvc.perform(get("/terminals")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void testGetFindById() throws Exception {
        when(terminalService.findById(terminal.getId())).thenReturn(terminal);
        mvc.perform(get("/terminals/{id}", terminal.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

/*    @Test
    public void testGetFindByTerminalNumber() throws Exception {
        when(terminalService.findByOwner(terminal.getOwnerId())).thenReturn(Arrays.asList(terminal));
        mvc.perform(get("/terminals/{terminalNumber}", terminal.getOwnerId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }*/

    @Test
    public void testDelete() throws Exception {
        mvc.perform(delete("/terminals/{id}", terminal.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
