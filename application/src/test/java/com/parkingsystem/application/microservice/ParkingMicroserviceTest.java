package com.parkingsystem.application.microservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingsystem.domain.errors.ParkingError;
import com.parkingsystem.domain.model.management.ParkingLotEntity;
import com.parkingsystem.domain.model.management.UserEntity;
import com.parkingsystem.domain.model.parking.SessionEntity;
import com.parkingsystem.domain.repository.ParkingLotRepository;
import com.parkingsystem.domain.repository.SessionRepository;
import com.parkingsystem.domain.repository.UserRepository;
import com.parkingsystem.infrastructure.api.v1.pakingasset.CloseSessionApi;
import com.parkingsystem.infrastructure.api.v1.pakingasset.NewSessionApiRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class ParkingMicroserviceTest {
    private final String endpointUrl = "/pms/v1/assets/";
    private final String parkingLotAddress = "testUrl";
    private final String notExistParkingLotAddress = "testUrlNotExist";
    private final String licenseNumber = "123XYZ";
    private final String fakeLicenseNumber = "fakeXYZ";
    private final Integer updateInterval = 15;
    private final BigDecimal tariff = BigDecimal.valueOf(3.00);
    private final BigDecimal minimalAmount = BigDecimal.valueOf(30);
    private final BigDecimal minimalAmountForCredit = BigDecimal.valueOf(15);
    private final String email = "test@email.com";
    private final String testPassword = "testPassword";
    private final String firstName = "TestFirstName";
    private final String lastName = "TestLastName";
    private final BigDecimal userBalance0 = BigDecimal.valueOf(0);
    private final BigDecimal userBalance20 = BigDecimal.valueOf(20);
    private final BigDecimal userBalance30 = BigDecimal.valueOf(30);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    private UserEntity createNewUser(BigDecimal userBalance) {
        UserEntity user = new UserEntity(email, testPassword, licenseNumber, firstName, lastName);
        user.setBalance(userBalance);
        userRepository.save(user);
        return user;
    }

    private void saveNewParkingLot(String parkingLotAddress, boolean isEnabled) {
        parkingLotRepository.save(new ParkingLotEntity(parkingLotAddress, isEnabled));
    }

    private void saveNewSession(UserEntity user, Integer updateInterval, BigDecimal tariff, BigDecimal minimalAmount, BigDecimal minimalAmountForCredit, String licensePlateNumber) {
        SessionEntity session = new SessionEntity(user, updateInterval, tariff, user.getBalance(), minimalAmount, minimalAmountForCredit, licensePlateNumber);
        sessionRepository.save(session);
    }

    private void saveNewEndedSession(UserEntity user, Integer updateInterval, BigDecimal tariff, BigDecimal minimalAmount, BigDecimal minimalAmountForCredit, String licensePlateNumber) {
        SessionEntity session = new SessionEntity(user, updateInterval, tariff, user.getBalance(), minimalAmount, minimalAmountForCredit, licensePlateNumber);
        session.setEndedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }

    @ExtendWith(SpringExtension.class)
    @ActiveProfiles("test")
    @SpringBootTest
    @Nested
    @DisplayName("Open session")
    class OpenSessionTest {

        @Test
        void error_1001_test() throws Exception {
            NewSessionApiRequest request = new NewSessionApiRequest("");
            String body = (new ObjectMapper()).valueToTree(request).toString();
            mockMvc.perform(
                    MockMvcRequestBuilders.post(endpointUrl + "/" + parkingLotAddress + "/sessions")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(print())
                    .andExpect(jsonPath("description").value(ParkingError.IS_EMPTY_LICENSE_NUMBER_1001.getDescription()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().is(ParkingError.IS_EMPTY_LICENSE_NUMBER_1001.getHttpStatus().getCode()));
        }

        @Test
        void error_1002_test() throws Exception {
            NewSessionApiRequest request = new NewSessionApiRequest(fakeLicenseNumber);
            String body = (new ObjectMapper()).valueToTree(request).toString();
            mockMvc.perform(
                    MockMvcRequestBuilders.post(endpointUrl + "/" + notExistParkingLotAddress + "/sessions")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(print())
                    .andExpect(jsonPath("description").value(ParkingError.PARKING_ADDRESS_IS_NOT_EXIST_1002.getDescription()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().is(ParkingError.PARKING_ADDRESS_IS_NOT_EXIST_1002.getHttpStatus().getCode()));
        }

        @Test
        @Transactional
        @Rollback
        void error_1003_test() throws Exception {
            saveNewParkingLot(parkingLotAddress, Boolean.TRUE);
            NewSessionApiRequest request = new NewSessionApiRequest(fakeLicenseNumber);
            String body = (new ObjectMapper()).valueToTree(request).toString();
            mockMvc.perform(
                    MockMvcRequestBuilders.post(endpointUrl + "/" + parkingLotAddress + "/sessions")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(print())
                    .andExpect(jsonPath("description").value(ParkingError.LICENSE_NUMBER_NOT_EXIST_1003.getDescription()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().is(ParkingError.LICENSE_NUMBER_NOT_EXIST_1003.getHttpStatus().getCode()));
        }

        @Test
        @Transactional
        @Rollback
        void error_1007_test() throws Exception {
            saveNewParkingLot(parkingLotAddress, Boolean.FALSE);
            NewSessionApiRequest request = new NewSessionApiRequest(licenseNumber);
            String body = (new ObjectMapper()).valueToTree(request).toString();
            mockMvc.perform(
                    MockMvcRequestBuilders.post(endpointUrl + "/" + parkingLotAddress + "/sessions")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(print())
                    .andExpect(jsonPath("description").value(ParkingError.PARKING_LOT_IS_NOT_WORKING_1007.getDescription()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().is(ParkingError.PARKING_LOT_IS_NOT_WORKING_1007.getHttpStatus().getCode()));
        }

        @Test
        @Transactional
        @Rollback
        void error_1004_test() throws Exception {
            saveNewParkingLot(parkingLotAddress, Boolean.TRUE);
            UserEntity user = createNewUser(userBalance0);
            saveNewSession(user, updateInterval, tariff, minimalAmount, minimalAmountForCredit, licenseNumber);

            NewSessionApiRequest request = new NewSessionApiRequest(licenseNumber);
            String body = (new ObjectMapper()).valueToTree(request).toString();
            mockMvc.perform(
                    MockMvcRequestBuilders.post(endpointUrl + "/" + parkingLotAddress + "/sessions")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(print())
                    .andExpect(jsonPath("description").value(ParkingError.LICENSE_NUMBER_HAVE_OPEN_SESSION_1004.getDescription()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().is(ParkingError.LICENSE_NUMBER_HAVE_OPEN_SESSION_1004.getHttpStatus().getCode()));
        }

        @Test
        @Transactional
        @Rollback
        void error_1005_test() throws Exception {
            saveNewParkingLot(parkingLotAddress, Boolean.TRUE);
            createNewUser(userBalance0);
            NewSessionApiRequest request = new NewSessionApiRequest(licenseNumber);
            String body = (new ObjectMapper()).valueToTree(request).toString();
            mockMvc.perform(
                    MockMvcRequestBuilders.post(endpointUrl + "/" + parkingLotAddress + "/sessions")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(print())
                    .andExpect(jsonPath("description").value(ParkingError.USER_BALANCE_TOO_LOW_FOR_OPEN_SESSION_1005.getDescription()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().is(ParkingError.USER_BALANCE_TOO_LOW_FOR_OPEN_SESSION_1005.getHttpStatus().getCode()));
        }

        @Test
        @Transactional
        @Rollback
        void error_1006_test() throws Exception {
            saveNewParkingLot(parkingLotAddress, Boolean.TRUE);
            UserEntity user = createNewUser(userBalance0);
            saveNewEndedSession(user, updateInterval, tariff, minimalAmount, minimalAmountForCredit, licenseNumber);
            NewSessionApiRequest request = new NewSessionApiRequest(licenseNumber);
            String body = (new ObjectMapper()).valueToTree(request).toString();
            mockMvc.perform(
                    MockMvcRequestBuilders.post(endpointUrl + "/" + parkingLotAddress + "/sessions")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(print())
                    .andExpect(jsonPath("description").value(ParkingError.CREDIT_LIMIT_TO_BIG_FOR_OPEN_SESSION_1006.getDescription()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().is(ParkingError.CREDIT_LIMIT_TO_BIG_FOR_OPEN_SESSION_1006.getHttpStatus().getCode()));
        }


        @Test
        @Transactional
        @Rollback
        void validTestWithoutSessions() throws Exception {
            saveNewParkingLot(parkingLotAddress, Boolean.TRUE);
            createNewUser(userBalance30);
            NewSessionApiRequest request = new NewSessionApiRequest(licenseNumber);
            String body = (new ObjectMapper()).valueToTree(request).toString();
            mockMvc.perform(
                    MockMvcRequestBuilders.post(endpointUrl + "/" + parkingLotAddress + "/sessions")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @Transactional
        @Rollback
        void validTest() throws Exception {
            saveNewParkingLot(parkingLotAddress, Boolean.TRUE);
            UserEntity user = createNewUser(userBalance20);
            saveNewEndedSession(user, updateInterval, tariff, minimalAmount, minimalAmountForCredit, licenseNumber);
            NewSessionApiRequest request = new NewSessionApiRequest(licenseNumber);
            String body = (new ObjectMapper()).valueToTree(request).toString();
            mockMvc.perform(
                    MockMvcRequestBuilders.post(endpointUrl + "/" + parkingLotAddress + "/sessions")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @ExtendWith(SpringExtension.class)
    @ActiveProfiles("test")
    @SpringBootTest
    @Nested
    @DisplayName("Close session")
    class CloseSessionTest {
        private final String statusClosedSession = "stopped";
        private final String fakeStatusClosedSession = "RUN_FOREST";

        @Test
        void IS_EMPTY_STATUS_1051_TEST() throws Exception {
            CloseSessionApi request = new CloseSessionApi();
            String body = (new ObjectMapper()).valueToTree(request).toString();
            mockMvc.perform(
                    MockMvcRequestBuilders.post(endpointUrl + parkingLotAddress + "/vehicle/" + licenseNumber + "/session")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(print())
                    .andExpect(jsonPath("description").value(ParkingError.IS_EMPTY_STATUS_1051.getDescription()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().is(ParkingError.IS_EMPTY_STATUS_1051.getHttpStatus().getCode()));
        }

        @Test
        void IS_NOT_STOPPED_STATUS_1053_TEST() throws Exception {
            CloseSessionApi request = new CloseSessionApi(fakeStatusClosedSession);
            String body = (new ObjectMapper()).valueToTree(request).toString();
            mockMvc.perform(
                    MockMvcRequestBuilders.post(endpointUrl + parkingLotAddress + "/vehicle/" + licenseNumber + "/session")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(print())
                    .andExpect(jsonPath("description").value(ParkingError.IS_NOT_STOPPED_STATUS_1053.getDescription()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().is(ParkingError.IS_NOT_STOPPED_STATUS_1053.getHttpStatus().getCode()));
        }

        @Test
        @Transactional
        @Rollback
        void PARKING_ADDRESS_IS_NOT_EXIST_1054_TEST() throws Exception {
            CloseSessionApi request = new CloseSessionApi(statusClosedSession);
            String body = (new ObjectMapper()).valueToTree(request).toString();
            mockMvc.perform(
                    MockMvcRequestBuilders.post(endpointUrl + notExistParkingLotAddress + "/vehicle/" + licenseNumber + "/session")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(print())
                    .andExpect(jsonPath("description").value(ParkingError.PARKING_ADDRESS_IS_NOT_EXIST_1054.getDescription()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().is(ParkingError.PARKING_ADDRESS_IS_NOT_EXIST_1054.getHttpStatus().getCode()));
        }

        @Test
        @Transactional
        @Rollback
        void LICENSE_NUMBER_NO_HAVE_OPEN_SESSION_1055_TEST() throws Exception {
            UserEntity user = createNewUser(userBalance0);
            saveNewParkingLot(parkingLotAddress, Boolean.TRUE);
            saveNewSession(user, updateInterval, tariff, minimalAmount, minimalAmountForCredit, licenseNumber);
            CloseSessionApi request = new CloseSessionApi(statusClosedSession);
            String body = (new ObjectMapper()).valueToTree(request).toString();
            mockMvc.perform(
                    MockMvcRequestBuilders.post(endpointUrl + parkingLotAddress + "/vehicle/" + fakeLicenseNumber + "/session")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(print())
                    .andExpect(jsonPath("description").value(ParkingError.LICENSE_NUMBER_NO_HAVE_OPEN_SESSION_1055.getDescription()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().is(ParkingError.LICENSE_NUMBER_NO_HAVE_OPEN_SESSION_1055.getHttpStatus().getCode()));
        }

        @Test
        @Transactional
        @Rollback
        void validTest() throws Exception {
            saveNewParkingLot(parkingLotAddress, Boolean.TRUE);
            UserEntity user = createNewUser(userBalance30);
            saveNewSession(user, updateInterval, tariff, minimalAmount, minimalAmountForCredit, licenseNumber);
            CloseSessionApi request = new CloseSessionApi(statusClosedSession);
            String body = (new ObjectMapper()).valueToTree(request).toString();
            mockMvc.perform(
                    MockMvcRequestBuilders.post(endpointUrl + parkingLotAddress + "/vehicle/" + licenseNumber + "/session")
                            .content(body)
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andDo(print())
                    .andExpect(jsonPath("status").value("stopped"))
                    .andExpect(jsonPath("total").isNotEmpty())
                    .andExpect(jsonPath("startedAt").isNotEmpty())
                    .andExpect(jsonPath("stoppedAt").isNotEmpty())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().isOk());
        }
    }
}
