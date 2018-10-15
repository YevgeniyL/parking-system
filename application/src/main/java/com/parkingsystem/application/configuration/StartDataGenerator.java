package com.parkingsystem.application.configuration;

import com.parkingsystem.domain.model.management.ParkingLotEntity;
import com.parkingsystem.domain.model.management.UserEntity;
import com.parkingsystem.domain.model.parking.NewSession;
import com.parkingsystem.domain.model.parking.SessionEntity;
import com.parkingsystem.domain.repository.ParkingLotRepository;
import com.parkingsystem.domain.repository.UserRepository;
import com.parkingsystem.domain.sevice.ApiVersion;
import com.parkingsystem.domain.sevice.parking.ParkingService;
import com.parkingsystem.infrastructure.repository.SessionSimpleRepository;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@Profile("h2-dev")
@Slf4j
public class StartDataGenerator {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParkingService parkingService;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private SessionSimpleRepository sessionSimpleRepository;

    private final String address = "address";

    @PostConstruct
    public void generateData() {
        List<ParkingLotEntity> parkingLotEntities = generateAddress(10);
        List<UserEntity> userEntities = generateUsers(30);
        List<SessionEntity> sessionEntities = generateSessions(parkingLotEntities, userEntities);
        List<UserEntity> emptyUsers = generateUsers(5);
        printAllData(parkingLotEntities, userEntities, sessionEntities, emptyUsers);
    }

    private List<SessionEntity> generateSessions(List<ParkingLotEntity> parkingLotEntities, List<UserEntity> userEntities) {
        int parkingSize = parkingLotEntities.size();
        int usersSize = userEntities.size();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            try {
                NewSession newSession = new NewSession(userEntities.get(random.nextInt(usersSize)).getLicensePlateNumber());
                parkingService.createSession(ApiVersion.V1, newSession, parkingLotEntities.get(random.nextInt(parkingSize)).getAddress());
            } catch (Exception e) {
            }
        }

        List<SessionEntity> allSessions = sessionSimpleRepository.findAll();
        for (SessionEntity session : allSessions) {
            session.setStartedAt(LocalDateTime.now().minusMinutes(random.nextInt(1000)));
            sessionSimpleRepository.save(session);
        }
        return allSessions;
    }

    private List<UserEntity> generateUsers(int count) {
        Fairy fairy;
        Person person;
        List<UserEntity> userEntities = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            fairy = Fairy.create();
            person = fairy.person();
            UserEntity userEntity = new UserEntity(person.getEmail(), person.getPassword(), person.getPassportNumber(), person.getFirstName(), person.getLastName());

            //set random balance
            if (i % 10 != 0) {
                userEntity.setBalance(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(-30, 100)));
            }

            userRepository.save(userEntity);
            userEntities.add(userEntity);
        }
        return userEntities;
    }

    private List<ParkingLotEntity> generateAddress(int count) {
        Random random = new Random();
        List<ParkingLotEntity> parkingLotEntityList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ParkingLotEntity parkingLotEntity = new ParkingLotEntity(i + address, random.nextBoolean());
            parkingLotRepository.save(parkingLotEntity);
            parkingLotEntityList.add(parkingLotEntity);
        }
        return parkingLotEntityList;
    }

    private void printAllData(List<ParkingLotEntity> parkingLotEntities, List<UserEntity> userEntities, List<SessionEntity> sessionEntities, List<UserEntity> emptyUsers) {
        log.error("");
        log.error("");
        log.error("");
        log.error("------------------- Start print generated data ------------------------");
        log.error("");
        log.error("");
        log.error("------------------- Parking lot ------------------------");
        Arrays.stream(parkingLotEntities.toArray()).forEach(p -> log.info(String.join(" ", p.toString())));
        log.error("");
        log.error("");
        log.error("------------------- User  ------------------------");
        Arrays.stream(userEntities.toArray()).forEach(p -> log.info(String.join(" ", p.toString())));
        log.error("");
        log.error("");
        log.error("------------------- Sessions  ------------------------");
        Arrays.stream(sessionEntities.toArray()).forEach(p -> log.info(String.join(" ", p.toString())));
        log.error("");
        log.error("");
        log.error("------------------- Empty Users  ------------------------");
        Arrays.stream(emptyUsers.toArray()).forEach(p -> log.info(String.join(" ", p.toString())));
        log.error("");
        log.error("");
        log.error("------------------- End print generated data ------------------------");
        log.error("");
        log.error("");
        log.error("");
    }
}
