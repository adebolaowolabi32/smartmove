package com.interswitch.smartmoveserver.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.model.Schedule;
import com.interswitch.smartmoveserver.model.Trip;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.service.TripService;
import com.interswitch.smartmoveserver.util.RandomUtil;
import org.junit.jupiter.api.AfterAll;
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
@ContextConfiguration(classes = TripApi.class)
public class TripApiTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TripService tripService;

    private Trip trip;

    @BeforeAll
    public void setup() {
        trip = new Trip();
        trip.setReferenceNo(new RandomUtil(5).nextString());
        trip.setDriver(new User());
        trip.setFare(5000);
        trip.setVehicle(new Vehicle());
        trip.setSchedule(new Schedule());
    }

    @AfterAll
    public void tearDown() {

    }

    @Test
    private void testFindAll() throws Exception {
        when(tripService.findAll()).thenReturn(Arrays.asList(trip, new Trip()));
        mvc.perform(get("/api/trips")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    private void testSave() throws Exception {
        when(tripService.save(trip)).thenReturn(trip);
        mvc.perform(post("/api/trips")
                .content(new ObjectMapper().writeValueAsString(trip))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    private void testFindById() throws Exception {
        when(tripService.findById(trip.getId())).thenReturn(trip);
        mvc.perform(get("/api/trips/{id}", trip.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }


    @Test
    private void testDelete() throws Exception {
        mvc.perform(delete("/api/trips/{id}", trip.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    private void testUpdate() throws Exception {
        when(tripService.update(trip)).thenReturn(trip);
        mvc.perform(put("/api/cards")
                .content(new ObjectMapper().writeValueAsString(trip))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }


}
