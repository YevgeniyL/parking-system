package com.parkingsystem.infrastructure.api.v1.management;

import com.parkingsystem.domain.model.management.NewParkingLot;
import org.springframework.stereotype.Component;

@Component
public class ManagementTransformer {

    public static NewParkingLot toDomain(NewParkingLotApiRequest request) {
        return new NewParkingLot(request.getAddress(), request.getIsEnabled()
        );
    }
}
