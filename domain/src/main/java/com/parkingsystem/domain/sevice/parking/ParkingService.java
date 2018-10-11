package com.parkingsystem.domain.sevice.parking;

import com.parkingsystem.domain.model.parking.NewSession;
import com.parkingsystem.domain.sevice.ApiVersion;

public interface ParkingService {

    void createSession(ApiVersion apiVersion, NewSession newSession, String parkingAddress);
}
