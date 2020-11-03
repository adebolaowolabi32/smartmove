package com.interswitch.smartmoveserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

/**
 * @author adebola.owolabi
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class SmartMoveApplication {


    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        SpringApplication.run(SmartMoveApplication.class, args);
    }

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }
}