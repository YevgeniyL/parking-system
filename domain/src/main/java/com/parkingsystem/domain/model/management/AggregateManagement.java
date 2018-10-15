package com.parkingsystem.domain.model.management;

import com.parkingsystem.domain.errors.ManagementError;
import com.parkingsystem.domain.repository.ParkingLotRepository;
import com.parkingsystem.domain.sevice.ApiVersion;
import com.parkingsystem.domain.sevice.management.ManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

@Service
@Slf4j
public class AggregateManagement implements ManagementService {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    public void save(ApiVersion apiVersion, NewParkingLot newParkingLot) {
        final String newAddress = newParkingLot.getAddress();
        final Boolean newEnabled = newParkingLot.getIsEnabled();

        if (StringUtils.isEmpty(newAddress)) {
            ManagementError.IS_EMPTY_ADDRESS_2001.doThrow();
        }
        if (StringUtils.isEmpty(newEnabled))
            ManagementError.IS_EMPTY_ENABLED_2002.doThrow();
        if (ApiVersion.V2.equals(apiVersion)) log.info("execute some api v2 logic");
        ParkingLotEntity existParkingEntity = parkingLotRepository.findBy(newAddress);

        if (existParkingEntity != null) {
            existParkingEntity.setAddress(newAddress);
            existParkingEntity.setIsEnabled(newEnabled);
            parkingLotRepository.save(existParkingEntity);
            log.info(MessageFormat.format("ParkingLot with Id=[{0}] was updated", existParkingEntity.getId()));
            return;
        }

        ParkingLotEntity parkingLotEntity = new ParkingLotEntity(newAddress, newEnabled);
        parkingLotRepository.save(parkingLotEntity);
        log.info("ParkingLot saved. Id=" + parkingLotEntity.getId());
    }
}
