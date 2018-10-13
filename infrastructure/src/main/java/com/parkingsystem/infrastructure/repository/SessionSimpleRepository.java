package com.parkingsystem.infrastructure.repository;

import com.parkingsystem.domain.model.parking.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionSimpleRepository extends JpaRepository<SessionEntity, Long> {

    SessionEntity findFirstByLicensePlateNumberAndEndedAtIsNullOrderByIdDesc(String licensePlateNumber);

}
