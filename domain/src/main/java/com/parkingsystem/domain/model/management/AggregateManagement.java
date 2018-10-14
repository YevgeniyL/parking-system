package com.parkingsystem.domain.model.management;

import com.parkingsystem.domain.errors.ManagementError;
import com.parkingsystem.domain.repository.ParkingLotRepository;
import com.parkingsystem.domain.sevice.ApiVersion;
import com.parkingsystem.domain.sevice.management.ManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class AggregateManagement implements ManagementService {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    public void save(ApiVersion apiVersion, NewParkingLot newParkingLot) {

        if (StringUtils.isEmpty(newParkingLot.getAddress()))
            ManagementError.IS_EMPTY_ADDRESS_2001.doThrow();
        if (StringUtils.isEmpty(newParkingLot.getIsEnabled()))
            ManagementError.IS_EMPTY_ENABLED_2002.doThrow();

        if (ApiVersion.V2.equals(apiVersion)) log.info("execute some api v2 logic");
        ParkingLotEntity parkingLotEntity = new ParkingLotEntity(newParkingLot.getAddress(), newParkingLot.getIsEnabled());
        parkingLotRepository.save(parkingLotEntity);
        log.info("ParkingLot saved. Id=" + parkingLotEntity.getId());
    }
}
