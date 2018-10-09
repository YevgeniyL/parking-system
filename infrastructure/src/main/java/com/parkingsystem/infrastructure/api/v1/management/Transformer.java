package com.parkingsystem.infrastructure.api.v1.management;

import com.parkingsystem.domain.model.ParkingLot.NewParkingLot;
import org.springframework.stereotype.Component;

@Component
public class Transformer {

    public NewParkingLot toDomain(NewParkingLotApiRequest request) {
        return new NewParkingLot(request.getUrl(), request.getIsEnabled()
        );
    }
}
