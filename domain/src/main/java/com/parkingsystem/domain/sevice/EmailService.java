package com.parkingsystem.domain.sevice;

import com.parkingsystem.domain.model.parking.EmailMessage;

public interface EmailService {

    void sendMessage(EmailMessage message);

    void sendAllNotSendedMessage();
}
