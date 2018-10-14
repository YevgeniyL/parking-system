package com.parkingsystem.infrastructure.api.v1.pakingasset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CloseSessionResponseApi {
    private String status;
    private BigDecimal total;
    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;
}
