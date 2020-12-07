package com.interswitch.smartmoveserver.config;

import com.interswitch.smartmoveserver.handler.UserLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * Created by adebola.owolabi on 12/7/2020
 */
@Component
public class LoginInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    UserLoginInterceptor userLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginInterceptor);
    }
}