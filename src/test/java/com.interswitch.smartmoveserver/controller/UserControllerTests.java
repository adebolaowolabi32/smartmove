package com.interswitch.smartmoveserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.helper.JwtHelper;
import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.UserService;
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
@ContextConfiguration(classes = { UserController.class, JwtHelper.class})
public class UserControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;
    
    private User user;

    @Before
    public void setup() {
        user = new User();
        long id = 10000023;
        user.setId(id);
        user.setFirstName("Alice");
        user.setLastName("Com");
        user.setType(Enum.UserType.BUS_OWNER);
        user.setEmail("alice@example.com");
        user.setMobileNo("123456789");
    }
    
/*    @Test
    public void testSave() throws Exception {
        when(userService.save(Enum.UserType.BUS_OWNER, "parent_id")).thenReturn(user);
        mvc.perform(post("/users/BUS_OWNER/parent_id")
                .content(new ObjectMapper().writeValueAsString(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(user)));

    }*/

    @Test
    public void testSave() throws Exception {
        when(userService.save(user)).thenReturn(user);
        mvc.perform(post("/users")
                .content(new ObjectMapper().writeValueAsString(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testUpdate() throws Exception {
        when(userService.update(user)).thenReturn(user);
        mvc.perform(put("/users")
                .content(new ObjectMapper().writeValueAsString(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testGetAll() throws Exception {
        when(userService.getAll()).thenReturn(Arrays.asList(user, new User()));
        mvc.perform(get("/users")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void testFindById() throws Exception {
        when(userService.findById(user.getId())).thenReturn(user);
        mvc.perform(get("/users/{id}", user.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }
/*

    @Test
    public void testFindByParent() throws Exception {
        when(userService.findByParent(user.getParentId())).thenReturn(Arrays.asList(user));
        mvc.perform(get("/users/{parentId}", user.getParentId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testFindByEmail() throws Exception {
        when(userService.findByEmail(user.getEmail())).thenReturn(user);
        mvc.perform(get("/users/{email}", user.getEmail())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }
    @Test
    public void testFindByMobile() throws Exception {
        when(userService.findByMobile(user.getMobileNo())).thenReturn(user);
        mvc.perform(get("/users/{mobileNo}", user.getMobileNo())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }
*/


    @Test
    public void testDelete() throws Exception {
        mvc.perform(delete("/users/{id}", user.getId())
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
