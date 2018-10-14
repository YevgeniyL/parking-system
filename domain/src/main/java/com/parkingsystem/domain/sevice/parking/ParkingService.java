package com.parkingsystem.domain.sevice.parking;

import com.parkingsystem.domain.model.parking.CloseSession;
import com.parkingsystem.domain.model.parking.CloseSessionResponse;
import com.parkingsystem.domain.model.parking.NewSession;
import com.parkingsystem.domain.sevice.ApiVersion;

import java.time.LocalDateTime;

public interface ParkingService {

    void createSession(ApiVersion apiVersion, NewSession newSession, String parkingAddress);

    CloseSessionResponse closeSession(ApiVersion apiVersion, CloseSession closeSessionRequest, String parkingAddress, String licencePlateNumber, LocalDateTime now);
}
