package com.parkingsystem.domain.model.management;

import com.parkingsystem.domain.errors.DomainException;
import com.parkingsystem.domain.errors.ErrorMessages;
import com.parkingsystem.domain.repository.ParkingLotRepository;
import com.parkingsystem.domain.sevice.ApiVersion;
import com.parkingsystem.domain.sevice.management.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AggregateManagement implements ManagementService {
    private static Logger logger = LoggerFactory.getLogger(AggregateManagement.class);

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    public void save(ApiVersion apiVersion, NewParkingLot newParkingLot) {
        if (StringUtils.isEmpty(newParkingLot.getAddress())) throw new DomainException(ErrorMessages.DOMAIN_ERROR_1001);
        if (StringUtils.isEmpty(newParkingLot.getIsEnabled())) throw new DomainException(ErrorMessages.DOMAIN_ERROR_1002);
        if (ApiVersion.V2.equals(apiVersion)) logger.info("execute some api v2 logic");
        parkingLotRepository.save(new ParkingLotEntity(newParkingLot.getAddress(),newParkingLot.getIsEnabled()));
    }
}
