package com.parkingsystem.domain.model.parking;

import com.parkingsystem.domain.errors.BaseError;
import com.parkingsystem.domain.errors.DomainException;
import com.parkingsystem.domain.errors.ParkingError;
import com.parkingsystem.domain.model.management.UserEntity;
import com.parkingsystem.domain.repository.UserRepository;
import com.parkingsystem.domain.sevice.ApiVersion;
import com.parkingsystem.domain.sevice.parking.ParkingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class AggregateParking implements ParkingService {

    @Autowired
    private UserRepository userRepository;
/*    @Autowired
    private SessionRepository sessionRepository;*/

    @Override
    public void createSession(ApiVersion apiVersion, NewSession newSession, String parkingAddress) {
        try {
            if (StringUtils.isEmpty(newSession.getLicensePlateNumber()))
                ParkingError.IS_EMPTY_LICENSE_NUMBER_1001.doThrow();
            if (StringUtils.isEmpty(parkingAddress))
                ParkingError.IS_EMPTY_ADDRESS_1002.doThrow();

            UserEntity user = userRepository.find(newSession.getLicensePlateNumber());
            if (user == null)
                ParkingError.LICENSE_NUMBER_NOT_EXIST_1003.doThrow();

            if (ApiVersion.V2.equals(apiVersion)) log.info("execute some api v2 logic");


            // TODO all logic

        } catch (DomainException e) {
            throw e;
        } catch (Exception e) {
            log.error("Saving process error", e);
            BaseError.INTERNAL_SERV_ERROR.doThrow();
        }
    }
}
