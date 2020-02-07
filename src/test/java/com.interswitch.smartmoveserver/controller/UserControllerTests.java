package com.interswitch.smartmoveserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.smartmoveserver.helper.JwtHelper;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.UserService;
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
@ContextConfiguration(classes = { UserController.class, JwtHelper.class})
public class UserControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void testSave() throws Exception {
        User user = new User();
        user.setType(User.UserType.BUS_OWNER);
        user.setParent("parent_id");
        user.setFirstName("user_firstname");
        user.setLastName("user_lastname");
        user.setEmailAddress("user_email@address.com");
        user.setMobileNumber("1234567890");

        when(userService.save(User.UserType.BUS_OWNER, "parent_id")).thenReturn(user);
        mvc.perform(post("/users/BUS_OWNER/parent_id")
                .content(new ObjectMapper().writeValueAsString(user))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(user)));

    }

}
