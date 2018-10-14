package com.parkingsystem.application;

import com.parkingsystem.domain.sevice.EmailService;
import components.EmailServiceImpl;
import components.SimplePriceEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.parkingsystem")
@EntityScan(basePackages = "com.parkingsystem.domain")
@EnableJpaRepositories(basePackages = {"com.parkingsystem.infrastructure.repository"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private EmailService emailService;

    @EventListener(ContextRefreshedEvent.class)
    public void sendAllEmails() {
        new Thread(() -> emailService.sendAllNotSendedMessage());
    }

    @Bean
    public SimplePriceEngine priceEngine() {
        return new SimplePriceEngine();
    }

    @Bean
    public EmailServiceImpl emailService() {
        return new EmailServiceImpl();
    }

}
