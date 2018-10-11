package com.parkingsystem.application.transformer;

import com.parkingsystem.domain.model.parking.NewSession;
import com.parkingsystem.infrastructure.api.v1.pakingasset.NewSessionApiRequest;
import com.parkingsystem.infrastructure.api.v1.pakingasset.ParkingTransformer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingTransformerTest {

    @Test
    void newSessionToDomain(){
        String licensePlateNumber = "123xxxx";
        ParkingTransformer parkingTransformer = new ParkingTransformer();
        NewSession newSession = parkingTransformer.toDomain(new NewSessionApiRequest(licensePlateNumber));
        assertEquals(licensePlateNumber,newSession.getLicensePlateNumber());
    }
}
