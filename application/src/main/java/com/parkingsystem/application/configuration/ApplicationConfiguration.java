package com.parkingsystem.application.configuration;

import components.EmailServiceImpl;
import components.SimplePriceEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public SimplePriceEngine priceEngine() {
        return new SimplePriceEngine();
    }

    @Bean
    public EmailServiceImpl emailService() {
        return new EmailServiceImpl();
    }

}
