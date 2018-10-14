package com.parkingsystem.application.repository;

import com.parkingsystem.domain.model.management.UserEntity;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    private final String email = "test@email.com";
    private final String testPassword = "testPassword";
    private final String firstName = "TestFirstName";
    private final String lastName = "TestLastName";
    private final String licensePlateNumber = "123XYZ";
    private final BigDecimal userBalance = BigDecimal.valueOf(15);
    @Autowired
    EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Rollback
    @Transactional
    void saveEmptyTest() {
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(new UserEntity()));
    }

    @Test
    @Rollback
    @Transactional
    void saveTest() {
        int count = ((Number) entityManager.createQuery("SELECT COUNT (*) FROM UserEntity ").getSingleResult()).intValue();
        assertEquals(0, count);
        UserEntity user = saveNewUser(email, testPassword, licensePlateNumber);
        count = ((Number) entityManager.createQuery("SELECT COUNT (*) FROM UserEntity ").getSingleResult()).intValue();
        assertEquals(1, count);
        assertEquals(email, user.getEmail());
        assertEquals(testPassword, user.getPassword());
        assertEquals(licensePlateNumber, user.getLicensePlateNumber());
        assertNotNull(user.getCreated());
    }

    @Test
    @Rollback
    @Transactional
    void findUserByLicensePlateNumberTest() {
        saveNewUser(email, testPassword, licensePlateNumber);
        UserEntity user = userRepository.find(null);
        assertNull(user);
        user = userRepository.find("33333");
        assertNull(user);
        user = userRepository.find(licensePlateNumber);
        assertEquals(email, user.getEmail());
        assertEquals(testPassword, user.getPassword());
        assertEquals(licensePlateNumber, user.getLicensePlateNumber());
        assertEquals(userBalance, user.getBalance());
    }


    private UserEntity saveNewUser(String email, String password, String licensePlateNumber) {
        UserEntity user = new UserEntity(email, password, licensePlateNumber, firstName, lastName);
        user.setBalance(userBalance);
        userRepository.save(user);
        return user;
    }
}
