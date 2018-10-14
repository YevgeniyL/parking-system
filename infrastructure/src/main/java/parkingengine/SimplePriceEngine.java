package parkingengine;

import com.parkingsystem.domain.sevice.PriceEngine;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class SimplePriceEngine implements PriceEngine {

    @Override
    public BigDecimal calc(LocalDateTime startSession, LocalDateTime endSession, Integer updateInterval, BigDecimal tariff) {
        final long durationInMinutes = Duration.between(startSession, endSession).toMinutes();
        final float updateIntervalCountFloat = (float) durationInMinutes / (float) updateInterval;
        final long updateIntervalCount = durationInMinutes / updateInterval;
        final float afterPoint = updateIntervalCountFloat - (float) updateIntervalCount;

        if (afterPoint > 0) {
            return tariff.multiply(BigDecimal.valueOf(updateIntervalCount + 1));
        } else
            return tariff.multiply(BigDecimal.valueOf(updateIntervalCount));
    }
}
