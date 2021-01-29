package com.interswitch.smartmoveserver;

import com.interswitch.smartmoveserver.helper.AuditorAwareImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

/**
 * @author adebola.owolabi
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
//This below helps to enable Jpa Auditing,auditorAwareRef value bears same name auditorAware bean defined in the class
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
//component scan base packages specified here helps to scan the bean components of isw-post-office library for visibility
//when doing dependency injection/autowiring
@ComponentScan(basePackages = {"com.interswitch", "com.interswitchng"})
public class SmartMoveApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartMoveApplication.class, args);
    }

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }

}