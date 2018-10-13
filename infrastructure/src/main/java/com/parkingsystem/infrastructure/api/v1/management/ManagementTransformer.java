package com.parkingsystem.infrastructure.api.v1.management;

import com.parkingsystem.domain.model.management.NewParkingLot;
import org.springframework.stereotype.Component;

@Component
public class ManagementTransformer {

    public NewParkingLot toDomain(NewParkingLotApiRequest request) {
        return new NewParkingLot(request.getAddress(), request.getIsEnabled()
        );
    }
}
