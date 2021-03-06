package com.parkingsystem.application.repository;

import com.parkingsystem.domain.model.management.ParkingLotEntity;
import com.parkingsystem.domain.model.management.UserEntity;
import com.parkingsystem.domain.model.parking.SessionEntity;
import com.parkingsystem.domain.repository.ParkingLotRepository;
import com.parkingsystem.domain.repository.SessionRepository;
import com.parkingsystem.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class SessionRepositoryTest {
    private final Integer updateInterval = 15;
    private final BigDecimal tariff = BigDecimal.valueOf(3.00);
    private final BigDecimal minimalAmount = BigDecimal.valueOf(15.00);
    private final BigDecimal minimalAmountForCredit = BigDecimal.valueOf(30.00);
    private final String email = "test@email.com";
    private final String testPassword = "testPassword";
    private final String firstName = "TestFirstName";
    private final String lastName = "TestLastName";
    private final String licensePlateNumber = "123XYZ";
    private final String fakeLicensePlateNumber = "fake123XYZ";
    private final BigDecimal userBalance = BigDecimal.valueOf(15);
    @Autowired
    EntityManager entityManager;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    private final String parkingLotAddress = "testUrl";

    @Test
    @Rollback
    void saveEmptySessionTest() {
        assertThrows(DataIntegrityViolationException.class, () -> sessionRepository.save(new SessionEntity()));
    }

    @Test
    @Rollback
    @Transactional
    void saveSessionTest() {
        int count = ((Number) entityManager.createQuery("SELECT COUNT (*) FROM SessionEntity").getSingleResult()).intValue();
        assertEquals(0, count);
        UserEntity user = createNewUser();
        ParkingLotEntity parkingLotEntity = saveNewParkingLot(parkingLotAddress, Boolean.TRUE);
        SessionEntity session = saveNewSession(user, parkingLotEntity, updateInterval, tariff, minimalAmountForCredit, licensePlateNumber);
        count = ((Number) entityManager.createQuery("SELECT COUNT (*) FROM SessionEntity").getSingleResult()).intValue();
        assertEquals(1, count);
        assertNotNull(session);
        assertEquals(licensePlateNumber, session.getLicensePlateNumber());
        assertEquals(tariff, session.getTariff());
        assertEquals(updateInterval, session.getRoundInterval());
        assertEquals(userBalance, session.getUserBalance());
        assertEquals(minimalAmountForCredit, session.getMinimalCreditAmount());
        assertNotNull(session.getStartedAt());
        assertEquals(user, session.getUser());
    }

    @Test
    @Rollback
    @Transactional
    void findSessionByLicensePlateNumberTest() {
        ParkingLotEntity parkingLotEntity = saveNewParkingLot(parkingLotAddress, Boolean.TRUE);
        SessionEntity session = saveNewSession(createNewUser(), parkingLotEntity, updateInterval, tariff, minimalAmountForCredit, licensePlateNumber);
        SessionEntity sessionByLicPlateNumber = sessionRepository.findNotClosedBy(licensePlateNumber);
        assertNotNull(sessionByLicPlateNumber);
        assertEquals(session, sessionByLicPlateNumber);
        assertEquals(session.getLicensePlateNumber(), sessionByLicPlateNumber.getLicensePlateNumber());

        sessionByLicPlateNumber = sessionRepository.findNotClosedBy(fakeLicensePlateNumber);
        assertNull(sessionByLicPlateNumber);

        session.setEndedAt(LocalDateTime.now());
        sessionByLicPlateNumber = sessionRepository.findNotClosedBy(licensePlateNumber);
        assertNull(sessionByLicPlateNumber);
    }

    @Test
    @Rollback
    @Transactional
    void findAnyOneByUserTest() {
        UserEntity user = createNewUser();
        SessionEntity anyOneSessionByUser = sessionRepository.findAnyOneByUser(user);
        assertNull(anyOneSessionByUser);
        ParkingLotEntity parkingLotEntity = saveNewParkingLot(parkingLotAddress, Boolean.TRUE);
        SessionEntity session = saveNewSession(user, parkingLotEntity, updateInterval, tariff, minimalAmountForCredit, licensePlateNumber);

        anyOneSessionByUser = sessionRepository.findAnyOneByUser(user);
        assertNotNull(anyOneSessionByUser);
        assertEquals(session, anyOneSessionByUser);
        assertEquals(session.getId(), anyOneSessionByUser.getId());

    }

    @Test
    @Rollback
    @Transactional
    void findStartedAndNotClosedSessionByPlateLicNumberTest() {
        UserEntity user = createNewUser();
        ParkingLotEntity parkingLotEntity = saveNewParkingLot(parkingLotAddress, Boolean.TRUE);
        SessionEntity sessionEntity = saveNewSession(user, parkingLotEntity, updateInterval, tariff, minimalAmountForCredit, licensePlateNumber);
        SessionEntity notExistSession = sessionRepository.findStartedAndNotClosedSessionBy(fakeLicensePlateNumber);
        assertNull(notExistSession);
        SessionEntity validSession = sessionRepository.findStartedAndNotClosedSessionBy(licensePlateNumber);
        assertNotNull(validSession);
        assertEquals(sessionEntity, validSession);
    }

    private UserEntity createNewUser() {
        UserEntity user = new UserEntity(email, testPassword, licensePlateNumber, firstName, lastName);
        user.setBalance(userBalance);
        userRepository.save(user);
        return user;
    }

    private SessionEntity saveNewSession(UserEntity user, ParkingLotEntity parkingLotEntity, Integer updateInterval, BigDecimal tariff, BigDecimal minimalAmountForCredit, String licensePlateNumber) {
        SessionEntity session = new SessionEntity(user, parkingLotEntity, updateInterval, tariff, user.getBalance(), minimalAmount, minimalAmountForCredit, licensePlateNumber);
        sessionRepository.save(session);
        return session;
    }

    private ParkingLotEntity saveNewParkingLot(String parkingLotAddress, boolean isEnabled) {
        ParkingLotEntity parkingLotEntity = new ParkingLotEntity(parkingLotAddress, isEnabled);
        parkingLotRepository.save(parkingLotEntity);
        return parkingLotEntity;
    }
}
