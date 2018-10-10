package com.parkingsystem.application.transformer;

import com.parkingsystem.domain.model.management.NewParkingLot;
import com.parkingsystem.infrastructure.api.v1.management.ManagementTransformer;
import com.parkingsystem.infrastructure.api.v1.management.NewParkingLotApiRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManagementTransformerTest {

    @Test
    void newParkingLotToDomain(){
        String address = "Address";
        boolean isEnabled = true;
        NewParkingLot parkingLot = ManagementTransformer.toDomain(new NewParkingLotApiRequest(address, isEnabled));
        assertEquals(address,parkingLot.getAddress());
        assertEquals(isEnabled,parkingLot.getIsEnabled());
    }
}
