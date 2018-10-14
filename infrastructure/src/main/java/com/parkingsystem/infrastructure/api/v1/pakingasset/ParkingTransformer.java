package com.parkingsystem.infrastructure.api.v1.pakingasset;

import com.parkingsystem.domain.model.parking.CloseSession;
import com.parkingsystem.domain.model.parking.CloseSessionResponse;
import com.parkingsystem.domain.model.parking.NewSession;
import org.springframework.stereotype.Component;

@Component
public class ParkingTransformer {
    public NewSession toDomain(NewSessionApiRequest request) {
        return new NewSession(request.getLicensePlateNumber());
    }

    public CloseSession toDomain(CloseSessionApi request) {
        return new CloseSession(request.getStatus());
    }

    public CloseSessionResponseApi toRest(CloseSessionResponse closeSession) {
        return new CloseSessionResponseApi(closeSession.getStatus(),
                closeSession.getTotal(),
                closeSession.getStartedAt(),
                closeSession.getStoppedAt());
    }
}
