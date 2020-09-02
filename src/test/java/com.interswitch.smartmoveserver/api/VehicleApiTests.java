package com.interswitch.smartmoveserver.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.model.Device;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.Vehicle;
import com.interswitch.smartmoveserver.service.VehicleService;
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

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = VehicleApi.class)
public class VehicleApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private VehicleService vehicleService;

    private Vehicle vehicle;

    @BeforeAll
    public void setup() {
        vehicle = new Vehicle();
        long id = 10000023;
        vehicle.setId(id);
        vehicle.setDevice(new Device());
        vehicle.setOwner(new User());
        vehicle.setRegNo("LAG0000002");
    }

    @Test
    public void testSave() throws Exception {
        when(vehicleService.save(vehicle)).thenReturn(vehicle);
        mvc.perform(post("/api/vehicles")
                .content(new ObjectMapper().writeValueAsString(vehicle))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testUpdate() throws Exception {
        when(vehicleService.update(vehicle)).thenReturn(vehicle);
        mvc.perform(put("/api/vehicles")
                .content(new ObjectMapper().writeValueAsString(vehicle))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

   /* @Test
    public void testfindAll() throws Exception {
        when(vehicleService.findAll()).thenReturn(Arrays.asList(vehicle, new Vehicle()));
        mvc.perform(get("/api/vehicles")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)));

    }*/

    @Test
    public void testFindById() throws Exception {
        when(vehicleService.findById(vehicle.getId())).thenReturn(vehicle);
        mvc.perform(get("/api/vehicles/{id}", vehicle.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

/*    @Test
    public void testFindByOwner() throws Exception {
        when(vehicleService.findByOwner(vehicle.getOwnerId())).thenReturn(Arrays.asList(vehicle));
        mvc.perform(get("/api/vehicles/{ownerId}", vehicle.getOwnerId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }*/

    @Test
    public void testDelete() throws Exception {
        mvc.perform(delete("/api/vehicles/{id}", vehicle.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
