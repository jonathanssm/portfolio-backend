package com.jonathanssm.portfoliobackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // Adiciona prefixo /api para todos os controllers REST
        configurer.addPathPrefix("/api", c -> c.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class));
    }
}
