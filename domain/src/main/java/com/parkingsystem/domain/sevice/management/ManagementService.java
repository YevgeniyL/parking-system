package com.parkingsystem.domain.sevice.management;


import com.parkingsystem.domain.model.ParkingLot.NewParkingLot;

public interface ManagementService {

    void save(NewParkingLot parkingLot, ApiVersion v1);

}
