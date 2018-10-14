package com.parkingsystem.application.repository;

import com.parkingsystem.domain.model.management.ParkingLotEntity;
import com.parkingsystem.domain.repository.ParkingLotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ParkingLotRepositoryTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Test
    @Rollback
    void saveTest() {
        int count = ((Number) entityManager.createQuery("SELECT COUNT (*) FROM ParkingLotEntity").getSingleResult()).intValue();
        assertEquals(0, count);
        assertThrows(DataIntegrityViolationException.class, () -> parkingLotRepository.save(new ParkingLotEntity()));
        parkingLotRepository.save(new ParkingLotEntity("someUrl", Boolean.TRUE));
        count = ((Number) entityManager.createQuery("SELECT COUNT (*) FROM ParkingLotEntity").getSingleResult()).intValue();
        assertEquals(1, count);
    }

}
