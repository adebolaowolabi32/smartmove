package com.interswitch.smartmoveserver.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.helper.JwtHelper;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.UserService;
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
@ContextConfiguration(classes = { UserApi.class, JwtHelper.class})
public class UserApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeAll
    public void setup() {
        user = new User();
        long id = 10000023;
        user.setId(id);
        user.setFirstName("Alice");
        user.setLastName("Com");
        user.setRole(Enum.Role.VEHICLE_OWNER);
        user.setEmail("alice@example.com");
        user.setMobileNo("123456789");
    }

    @Test
    public void testSave() throws Exception {
        when(userService.create(user, "")).thenReturn(user);
        mvc.perform(post("/api/users")
                .content(new ObjectMapper().writeValueAsString(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testfindAll() throws Exception {
        when(userService.findAll()).thenReturn(Arrays.asList(user, new User()));
        mvc.perform(get("/api/users")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void testFindById() throws Exception {
        when(userService.findById(user.getId(), "")).thenReturn(user);
        mvc.perform(get("/api/users/{id}", user.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(delete("/api/users/{id}", user.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
