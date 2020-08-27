package com.interswitch.smartmoveserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

/**
 * @author adebola.owolabi
 */
@SpringBootApplication
public class SmartMoveApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartMoveApplication.class, args);
    }

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }
}