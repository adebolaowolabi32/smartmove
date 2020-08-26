package com.interswitch.smartmoveserver.service;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.model.request.IswRole;
import com.interswitch.smartmoveserver.model.request.IswUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IswCoreServiceTests {
    User user;
    @Autowired
    private  IswCoreService iswCoreService;
    @Value("${spring.user.url}")
    private String userurl;
    @Value("${spring.user.roles}")
    private String roleurl;


    private IswUser iswUser;
    private IswRole iswRole;

    @BeforeAll
    public void  setup(){
        user = new  User();

        user.setRole(Enum.Role.AGENT);
        user.setUsername("username");
        user.setEnabled(false);
        user.setAddress("Lagos");
        user.setEmail("user@gmail.com");
        user.setLastName("roland");
        user.setFirstName("soul");
        user.setMobileNo("0800");

        iswRole = new IswRole();
        iswRole.setAppId(11517);
        iswRole.setDomainId(12780);
        iswRole.setName(user.getRole());

        iswUser = new IswUser();
        iswUser.setDomainCode("ISW");
        iswUser.setAppCode("smart-move");
        iswUser.setMobileNo("0800");
        iswUser.setEnabled(false);
        iswUser.setAddress("Lagos");
        iswUser.setEmail("user@gmail.com");
        iswUser.setUsername("username");
        iswUser.setLastName("roland");
        iswUser.setFirstName("soul");
        iswUser.setRoles(Collections.singleton(iswRole));
    }

    @Test
    public void testCreateUser(){
        IswUser savedUser = iswCoreService.createUser(user);
        assertEquals(savedUser, iswUser);
    }

    @Test
    public void testBuildUser(){
        IswUser response = iswCoreService.buildUser(user);
        assertEquals(response, iswUser);
    }
/*
    @Test
    public void testGetRoleByUsername(){
        Object response =  iswCoreService.getRolesByUsername(iswUser.getUsername());
        assertThat(response).isEqualToComparingFieldByField(iswUser);

    }*/

    @Test
    public void testGetUsersPermission() throws NoSuchFieldException, IllegalAccessException{
        Object response =  iswCoreService.getUserPermissions(iswUser.getUsername(),iswUser.getDomainCode(),iswUser.getAppCode());
        assertEquals(response, iswUser);
    }
}







