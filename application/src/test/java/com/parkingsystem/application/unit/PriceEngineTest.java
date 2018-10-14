package com.parkingsystem.application.unit;


import com.parkingsystem.domain.sevice.PriceEngine;
import org.junit.jupiter.api.Test;
import components.SimplePriceEngine;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceEngineTest {
    private final Integer updateInterval = 15;
    private final BigDecimal tariff = BigDecimal.valueOf(10.00);


    @Test
    void simplePriceCalculatingTest() {
        PriceEngine priceEngine = new SimplePriceEngine();

        BigDecimal calc = priceEngine.calc(LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), updateInterval, tariff);
        assertEquals(0, BigDecimal.valueOf(10).compareTo(calc));

        calc = priceEngine.calc(LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), updateInterval, tariff);
        assertEquals(0, BigDecimal.valueOf(10).compareTo(calc));

        calc = priceEngine.calc(LocalDateTime.now(), LocalDateTime.now().plusMinutes(20), updateInterval, tariff);
        assertEquals(0, BigDecimal.valueOf(20).compareTo(calc));

        calc = priceEngine.calc(LocalDateTime.now(), LocalDateTime.now().plusMinutes(30), updateInterval, tariff);
        assertEquals(0, BigDecimal.valueOf(20).compareTo(calc));

        calc = priceEngine.calc(LocalDateTime.now(), LocalDateTime.now().plusMinutes(31), updateInterval, tariff);
        assertEquals(0, BigDecimal.valueOf(30).compareTo(calc));
    }
}
