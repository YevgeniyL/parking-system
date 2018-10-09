package com.parkingsystem.domain.model.ParkingLot;

import com.parkingsystem.domain.repository.ParkingLotRepository;
import com.parkingsystem.domain.sevice.management.ApiVersion;
import com.parkingsystem.domain.sevice.management.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AggregateManagement implements ManagementService {
    private static Logger logger = LoggerFactory.getLogger(AggregateManagement.class);

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    public void save(NewParkingLot parkingLot, ApiVersion v1) {
        ParkingLotEntity parkingLotEntity = new ParkingLotEntity();
        if (ApiVersion.V2.equals(v1)) logger.info("execute some api v2 logic");
        parkingLotRepository.save(parkingLotEntity);
    }
}
