package com.parkingsystem.application.configuration;

import com.parkingsystem.domain.sevice.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Slf4j
@Configuration
public class ParkingSystemResendListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private EmailService emailService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Init send email after start system");
        new Thread(() -> emailService.sendAllNotSendedMessage()).run();
    }
}
