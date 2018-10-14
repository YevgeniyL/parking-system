package com.parkingsystem.domain.sevice;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PriceEngine {
    BigDecimal calc(LocalDateTime startSession, LocalDateTime endSession, Integer roundInterval, BigDecimal tariff);
}
