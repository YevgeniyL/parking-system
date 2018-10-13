package com.parkingsystem.domain.model.parking;

import com.parkingsystem.domain.errors.BaseError;
import com.parkingsystem.domain.errors.DomainException;
import com.parkingsystem.domain.errors.ParkingError;
import com.parkingsystem.domain.model.management.ParkingLotEntity;
import com.parkingsystem.domain.model.management.UserEntity;
import com.parkingsystem.domain.repository.ParkingLotRepository;
import com.parkingsystem.domain.repository.SessionRepository;
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
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Override
    public void createSession(ApiVersion apiVersion, NewSession newSession, String parkingAddress) {
        try {
            if (StringUtils.isEmpty(newSession.getLicensePlateNumber()))
                ParkingError.IS_EMPTY_LICENSE_NUMBER_1001.doThrow();

            ParkingLotEntity parkingLot = parkingLotRepository.findBy(parkingAddress);
            if (parkingLot == null)
                ParkingError.PARKING_ADDRESS_IS_NOT_EXIST_1002.doThrow();

            UserEntity user = userRepository.find(newSession.getLicensePlateNumber());
            if (user == null)
                ParkingError.LICENSE_NUMBER_NOT_EXIST_1003.doThrow();

            SessionEntity existSession = sessionRepository.findLastWhereEndedIsNullBy(newSession.getLicensePlateNumber());
            if (existSession != null) {
                log.error("System contain not ended parking session for user %s for licensePlateNumber %s", user.getEmail(), newSession.getLicensePlateNumber());
                ParkingError.LICENSE_NUMBER_HAVE_OPEN_SESSION_1004.doThrow();
            }
            if (ApiVersion.V2.equals(apiVersion)) log.info("execute some api v2 logic");

//TODO save session and get values from application.properties
//            SessionEntity session = new SessionEntity(user, updateInterval, tariff, user.getBalance(), licensePlateNumber);
//            sessionRepository.save(session);

        } catch (DomainException e) {
            throw e;
        } catch (Exception e) {
            log.error("Saving process error", e);
            BaseError.INTERNAL_SERV_ERROR.doThrow();
        }
    }
}
