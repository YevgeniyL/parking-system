package com.parkingsystem.infrastructure.repository;

import com.parkingsystem.domain.model.email.EmailMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailRepository extends JpaRepository<EmailMessageEntity, Long> {

    List<EmailMessageEntity> findBySendedIsNull();

}
