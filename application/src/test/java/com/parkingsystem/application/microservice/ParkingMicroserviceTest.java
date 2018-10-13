package com.parkingsystem.application.microservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingsystem.domain.errors.ParkingError;
import com.parkingsystem.domain.model.management.ParkingLotEntity;
import com.parkingsystem.domain.model.management.UserEntity;
import com.parkingsystem.domain.model.parking.SessionEntity;
import com.parkingsystem.domain.repository.ParkingLotRepository;
import com.parkingsystem.domain.repository.SessionRepository;
import com.parkingsystem.domain.repository.UserRepository;
import com.parkingsystem.infrastructure.api.v1.pakingasset.NewSessionApiRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class ParkingMicroserviceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    private final String parkingLotAddress = "testUrl";
    private final String notExistParkingLotAddress = "testUrl";
    private final String walidLicenseNumber = "123XYZ";
    private final String fakeLicenseNumber = "fakeXYZ";

    private final Integer updateInterval = 15;
    private final BigDecimal tariff = BigDecimal.valueOf(3.00);
    private final BigDecimal balance = BigDecimal.valueOf(15);
    private final String email = "test@email.com";
    private final String testPassword = "testPassword";
    private final BigDecimal userBalance =  BigDecimal.valueOf(15);

    @Test
    void error_1001_test() throws Exception {
        NewSessionApiRequest request = new NewSessionApiRequest("");
        String body = (new ObjectMapper()).valueToTree(request).toString();
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/pms/v1/assets/" + parkingLotAddress + "/sessions")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isPartialContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("description").value(ParkingError.IS_EMPTY_LICENSE_NUMBER_1001.getDescription()));
    }

    @Test
    void error_1002_test() throws Exception {

        NewSessionApiRequest request = new NewSessionApiRequest(fakeLicenseNumber);
        String body = (new ObjectMapper()).valueToTree(request).toString();
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/pms/v1/assets/" + notExistParkingLotAddress + "/sessions")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("description").value(ParkingError.PARKING_ADDRESS_IS_NOT_EXIST_1002.getDescription()));
    }

    @Test
    @Transactional
    @Rollback
    void error_1003_test() throws Exception {
        createParkingLot();
        NewSessionApiRequest request = new NewSessionApiRequest(fakeLicenseNumber);
        String body = (new ObjectMapper()).valueToTree(request).toString();
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/pms/v1/assets/" + parkingLotAddress + "/sessions")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("description").value(ParkingError.LICENSE_NUMBER_NOT_EXIST_1003.getDescription()));
    }

    @Test
    @Transactional
    @Rollback
    void error_1004_test() throws Exception {
        createParkingLot();
        UserEntity user = createNewUser();
        saveNewSession(user, updateInterval, tariff, walidLicenseNumber);

        NewSessionApiRequest request = new NewSessionApiRequest(walidLicenseNumber);
        String body = (new ObjectMapper()).valueToTree(request).toString();
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/pms/v1/assets/" + parkingLotAddress + "/sessions")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("description").value(ParkingError.LICENSE_NUMBER_HAVE_OPEN_SESSION_1004.getDescription()));
    }

    private void createParkingLot() {
        parkingLotRepository.save(new ParkingLotEntity(parkingLotAddress, Boolean.TRUE));
    }

    private UserEntity createNewUser() {
        UserEntity user = new UserEntity(email, testPassword, walidLicenseNumber);
        user.setBalance(userBalance);
        userRepository.save(user);
        return user;
    }

    private void saveNewSession(UserEntity user, Integer updateInterval, BigDecimal tariff, String licensePlateNumber) {
        SessionEntity session = new SessionEntity(user, updateInterval, tariff, user.getBalance(), licensePlateNumber);
        sessionRepository.save(session);
    }
}
