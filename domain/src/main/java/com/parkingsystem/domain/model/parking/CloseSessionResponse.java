package com.parkingsystem.domain.model.parking;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CloseSessionResponse {
    private String status;
    private BigDecimal total;
    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;
}
