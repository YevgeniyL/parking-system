package com.parkingsystem.domain.model.parking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {
    private String text;
    private String sendTo;
    private String subject;
    private Long sessionId;
}
