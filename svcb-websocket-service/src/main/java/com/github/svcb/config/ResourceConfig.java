package com.github.svcb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;

@Configuration
public class ResourceConfig {
    @Bean
    RouterFunction staticResourceLocator(){
        return RouterFunctions.resources("/socket/**", new ClassPathResource("static/"));
    }

}
