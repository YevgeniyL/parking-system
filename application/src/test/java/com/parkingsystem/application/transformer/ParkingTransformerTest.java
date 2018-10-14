package com.parkingsystem.application.transformer;

import com.parkingsystem.domain.model.parking.CloseSession;
import com.parkingsystem.domain.model.parking.CloseSessionResponse;
import com.parkingsystem.domain.model.parking.NewSession;
import com.parkingsystem.infrastructure.api.v1.pakingasset.CloseSessionApi;
import com.parkingsystem.infrastructure.api.v1.pakingasset.CloseSessionResponseApi;
import com.parkingsystem.infrastructure.api.v1.pakingasset.NewSessionApiRequest;
import com.parkingsystem.infrastructure.api.v1.pakingasset.ParkingTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class ParkingTransformerTest {

    @Autowired
    private ParkingTransformer parkingTransformer;

    @Test
    void newSessionToDomain() {
        String licensePlateNumber = "123xxxx";
        NewSession newSession = parkingTransformer.toDomain(new NewSessionApiRequest(licensePlateNumber));
        assertEquals(licensePlateNumber, newSession.getLicensePlateNumber());
    }

    @Test
    void closeSessionToDomain() {
        String status = "stopped";
        CloseSession session = parkingTransformer.toDomain(new CloseSessionApi(status));
        assertEquals(status, session.getStatus());
    }

    @Test
    void closeSessionToRest() {
        String status = "stopped";
        BigDecimal total = BigDecimal.valueOf(100.00);
        LocalDateTime startAt = LocalDateTime.now().minusHours(2);
        LocalDateTime endAt = LocalDateTime.now();
        CloseSessionResponseApi responseApi = parkingTransformer.toRest(new CloseSessionResponse(status, total, startAt, endAt));
        assertEquals(status, responseApi.getStatus());
        assertEquals(total, responseApi.getTotal());
        assertEquals(startAt, responseApi.getStartedAt());
        assertEquals(endAt, responseApi.getStoppedAt());
    }
}
