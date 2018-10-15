package com.parkingsystem.application.repository;

import com.parkingsystem.domain.model.management.ParkingLotEntity;
import com.parkingsystem.domain.repository.ParkingLotRepository;
import org.junit.jupiter.api.Assertions;
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
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ParkingLotRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    private final String address = "address";
    private final String fakeAdress = "fakeAddress";


    @Test
    @Rollback
    @Transactional
    void saveTest() {
        int count = ((Number) entityManager.createQuery("SELECT COUNT (*) FROM ParkingLotEntity").getSingleResult()).intValue();
        assertEquals(0, count);
        ParkingLotEntity parkingLotEntity = new ParkingLotEntity(address, Boolean.TRUE);
        parkingLotRepository.save(parkingLotEntity);
        count = ((Number) entityManager.createQuery("SELECT COUNT (*) FROM ParkingLotEntity").getSingleResult()).intValue();
        assertEquals(1, count);
    }

    @Test
    @Rollback
    @Transactional
    void findByTest() {
        ParkingLotEntity parkingLotEntity = new ParkingLotEntity(address, Boolean.TRUE);
        parkingLotRepository.save(parkingLotEntity);
        Assertions.assertNull(parkingLotRepository.findBy(fakeAdress));
        Assertions.assertNotNull(parkingLotRepository.findBy(address));
    }

}
