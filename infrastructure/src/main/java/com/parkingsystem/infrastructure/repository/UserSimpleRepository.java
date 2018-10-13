package com.parkingsystem.infrastructure.repository;

import com.parkingsystem.domain.model.management.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSimpleRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByLicensePlateNumber(String licensePlateNumber);
}
