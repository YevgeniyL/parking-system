package com.parkingsystem.domain.model.parking;

import com.parkingsystem.domain.sevice.ApiVersion;
import com.parkingsystem.domain.sevice.parking.ParkingService;
import org.springframework.stereotype.Service;

@Service
public class AggregateParking implements ParkingService {

    @Override
    public void createSession(ApiVersion apiVersion, NewSession newSession, int parkingId) {

    }
}
