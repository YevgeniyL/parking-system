package components;

import com.parkingsystem.domain.sevice.PriceEngine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

public class SimplePriceEngine implements PriceEngine {

    @Override
    public BigDecimal calc(LocalDateTime startSession, LocalDateTime endSession, Integer roundInterval, BigDecimal tariff) {
        final long durationInMinutes = Duration.between(startSession, endSession).toMinutes();
        final float updateIntervalCountFloat = (float) durationInMinutes / (float) roundInterval;
        final long updateIntervalCount = durationInMinutes / roundInterval;
        final float afterPoint = updateIntervalCountFloat - (float) updateIntervalCount;

        if (afterPoint > 0) {
            return tariff.multiply(BigDecimal.valueOf(updateIntervalCount + 1)).setScale(2, RoundingMode.HALF_UP);
        } else
            return tariff.multiply(BigDecimal.valueOf(updateIntervalCount)).setScale(2, RoundingMode.HALF_UP);
    }
}
