package com.interswitch.smartmoveserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
public class TemplateEngineConfig {

    @Autowired
    private TemplateEngine templateEngine;

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