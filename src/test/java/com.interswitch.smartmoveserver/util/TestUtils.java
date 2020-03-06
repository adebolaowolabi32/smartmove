package com.interswitch.smartmoveserver.util;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public final class TestUtils {
    public static User buildTestUser(){
        User user = new User();
        user.setFirstName("Alice");
        user.setLastName("Com");
        user.setRole(Enum.Role.VEHICLE_OWNER);
        user.setEmail("alice@example.com");
        user.setMobileNo("123456789");
        return user;
    }
}
