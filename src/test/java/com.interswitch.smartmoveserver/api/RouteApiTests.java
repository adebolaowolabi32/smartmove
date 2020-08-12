package com.interswitch.smartmoveserver.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.Route;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.RouteService;
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
@ContextConfiguration(classes = RouteApi.class)
public class RouteApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RouteService routeService;

    private Route route;

    @Before
    public void setup() {
        route = new Route();
        long id = 10000023;
        route.setId(id);
        route.setName("my_route");
        route.setType(Enum.TransportMode.RAIL);
        route.setOwner(new User());
        route.setStartTerminalId(123L);
        route.setStopTerminalId(345L);
        route.setPrice(500);
        route.setEnabled(true);
    }

    @Test
    public void testSave() throws Exception {
        when(routeService.save(route)).thenReturn(route);
        mvc.perform(post("/routes")
                .content(new ObjectMapper().writeValueAsString(route))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testUpdate() throws Exception {
        when(routeService.update(route)).thenReturn(route);
        mvc.perform(put("/routes")
                .content(new ObjectMapper().writeValueAsString(route))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testfindAll() throws Exception {
        when(routeService.findAll()).thenReturn(Arrays.asList(route, new Route()));
        mvc.perform(get("/routes")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void testGetFindById() throws Exception {
        when(routeService.findById(route.getId())).thenReturn(route);
        mvc.perform(get("/routes/{id}", route.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

/*    @Test
    public void testGetFindByRouteNumber() throws Exception {
        when(routeService.findByOwner(route.getOwnerId())).thenReturn(Arrays.asList(route));
        mvc.perform(get("/routes/{owner}", route.getOwnerId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }*/

    @Test
    public void testDelete() throws Exception {
        mvc.perform(delete("/routes/{id}", route.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
