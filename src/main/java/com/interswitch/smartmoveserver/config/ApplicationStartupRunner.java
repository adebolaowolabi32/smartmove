package com.interswitch.smartmoveserver.config;

import com.interswitch.smartmoveserver.model.Enum;
import com.interswitch.smartmoveserver.model.User;
import com.interswitch.smartmoveserver.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupRunner implements CommandLineRunner {
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    UserService userService;

    @Override
    public void run(String... args) throws Exception {

        User adminUser = new User();
        adminUser.setUsername("smart.move");
        adminUser.setFirstName("Smart");
        adminUser.setLastName("Move");
        adminUser.setEmail("smart.move@interswitch.com");
        adminUser.setMobileNo("0801234567");
        adminUser.setAddress("Lagos Nigeria");
        adminUser.setRole(Enum.Role.ISW_ADMIN);
        adminUser.setEnabled(true);
        userService.saveAdmin(adminUser);
        logger.info("System Administrator created successfully!");

        User user = new User();
        user.setUsername("adebola.owolabi");
        user.setFirstName("Adebola");
        user.setLastName("Owolabi");
        user.setEmail("adebola.owolabi@interswitchgroup.com");
        user.setMobileNo("08160275523");
        user.setAddress("Lagos Nigeria");
        user.setRole(Enum.Role.ISW_ADMIN);
        user.setEnabled(true);
        userService.save(user);
        logger.info("System Administrator 2 created successfully!");
    }
}