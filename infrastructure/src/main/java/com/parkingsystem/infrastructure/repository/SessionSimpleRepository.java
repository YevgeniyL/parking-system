package com.parkingsystem.infrastructure.repository;

import com.parkingsystem.domain.model.management.UserEntity;
import com.parkingsystem.domain.model.parking.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionSimpleRepository extends JpaRepository<SessionEntity, Long> {

    SessionEntity findFirstByLicensePlateNumberAndEndedAtIsNull(String licensePlateNumber);

    SessionEntity findFirstByUser(UserEntity user);

    SessionEntity findFirstByLicensePlateNumberAndStartedAtIsNotNullAndEndedAtIsNull (String licensePlateNumber);
}
