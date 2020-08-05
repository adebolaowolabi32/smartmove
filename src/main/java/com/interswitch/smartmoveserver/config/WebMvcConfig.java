package com.interswitch.smartmoveserver.config;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.webflow.view.AjaxThymeleafViewResolver;
import org.thymeleaf.spring5.webflow.view.FlowAjaxThymeleafView;

/**
 * @author adebola.owolabi
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /*@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ThymeleafLayoutInterceptor());
    }*/

    @Autowired
    private SpringResourceTemplateResolver templateResolver;
   /* @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }*/

    /* @Bean
     public SpringResourceTemplateResolver templateResolver(){
         SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
         return templateResolver;
     }
 */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(new LayoutDialect());
        templateEngine.addDialect(new SpringSecurityDialect());
        //templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Bean
    public AjaxThymeleafViewResolver viewResolver() {
        AjaxThymeleafViewResolver viewResolver = new AjaxThymeleafViewResolver();
        viewResolver.setViewClass(FlowAjaxThymeleafView.class);
        viewResolver.setTemplateEngine(templateEngine());
        return viewResolver;
    }
}
