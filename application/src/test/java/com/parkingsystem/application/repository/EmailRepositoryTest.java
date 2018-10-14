package com.parkingsystem.application.repository;

import com.parkingsystem.domain.model.email.EmailMessageEntity;
import com.parkingsystem.infrastructure.repository.EmailRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmailRepositoryTest {
    @Autowired
    EmailRepository emailRepository;

    @Test
    void findBySendedIsNull() {
        Assertions.assertEquals(0, emailRepository.findBySendedIsNull().size());
        EmailMessageEntity email = new EmailMessageEntity();
        email.setSended(LocalDateTime.now());
        email.setSubject("");
        email.setSendTo("");
        email.setText("");
        emailRepository.save(email);
        Assertions.assertEquals(0, emailRepository.findBySendedIsNull().size());
        email.setSended(null);
        emailRepository.save(email);
        Assertions.assertEquals(1, emailRepository.findBySendedIsNull().size());
    }
}
