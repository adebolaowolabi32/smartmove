package com.interswitch.smartmoveserver.config;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.webflow.view.AjaxThymeleafViewResolver;
import org.thymeleaf.spring5.webflow.view.FlowAjaxThymeleafView;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Locale;

/**
 * @author adebola.owolabi
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private SpringResourceTemplateResolver templateResolver;

    @Autowired
    private TemplateEngine templateEngine;


    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

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


    @PostConstruct
    public void remoteTemplateResolver() {

        final FileTemplateResolver resolver = new FileTemplateResolver();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("templates").getFile());

        String templateDir = file.getPath();

        resolver.setOrder(1);
        String pathToSearch = String.format("%s%s", templateDir, File.separator);

        System.out.println("PathToSearch===>"+pathToSearch);

        resolver.setPrefix(pathToSearch);
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(false);

        templateEngine.addTemplateResolver(resolver);
    }
}
