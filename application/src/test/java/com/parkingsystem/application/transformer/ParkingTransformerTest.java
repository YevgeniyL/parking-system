package com.parkingsystem.application.transformer;

import com.parkingsystem.domain.model.parking.NewSession;
import com.parkingsystem.infrastructure.api.v1.pakingasset.NewSessionApiRequest;
import com.parkingsystem.infrastructure.api.v1.pakingasset.ParkingTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
}
