package com.parkingsystem.infrastructure.api.v1.pakingasset;

import com.parkingsystem.domain.model.parking.NewSession;
import org.springframework.stereotype.Component;

@Component
public class ParkingTransformer {
    public static NewSession toDomain(NewSessionApiRequest request) {
        return new NewSession(request.getLicensePlateNumber());
    }
}
