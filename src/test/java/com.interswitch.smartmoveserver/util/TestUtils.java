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
        user.setEmail(new RandomUtil().nextString().concat("@example.com"));
        user.setMobileNo(RandomUtil.getRandomNumber(11));
        return user;
    }

    public static User buildTestUser(Enum.Role role){
        User user = new User();
        user.setFirstName("Alice");
        user.setLastName("Com");
        user.setRole(role);
        user.setEmail(new RandomUtil().nextString().concat("@example.com"));
        user.setMobileNo(RandomUtil.getRandomNumber(11));
        return user;
    }
}
