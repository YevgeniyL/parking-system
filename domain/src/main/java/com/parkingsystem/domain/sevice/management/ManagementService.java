package com.parkingsystem.domain.sevice.management;


import com.parkingsystem.domain.model.management.NewParkingLot;
import com.parkingsystem.domain.sevice.ApiVersion;

public interface ManagementService {

    void save(ApiVersion v1, NewParkingLot newParkingLot);

}
