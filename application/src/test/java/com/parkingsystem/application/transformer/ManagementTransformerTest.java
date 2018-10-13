package com.parkingsystem.application.transformer;

import com.parkingsystem.domain.model.management.NewParkingLot;
import com.parkingsystem.infrastructure.api.v1.management.ManagementTransformer;
import com.parkingsystem.infrastructure.api.v1.management.NewParkingLotApiRequest;
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
public class ManagementTransformerTest {

    @Autowired
    private ManagementTransformer managementTransformer;

    @Test
    void newParkingLotToDomain() {
        String address = "Address";
        boolean isEnabled = true;
        NewParkingLot parkingLot = managementTransformer.toDomain(new NewParkingLotApiRequest(address, isEnabled));
        assertEquals(address,parkingLot.getAddress());
        assertEquals(isEnabled,parkingLot.getIsEnabled());
    }
}
