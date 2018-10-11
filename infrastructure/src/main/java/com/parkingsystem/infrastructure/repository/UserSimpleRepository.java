package com.parkingsystem.infrastructure.repository;

import com.parkingsystem.domain.model.management.UserEntity;

public interface UserSimpleRepository extends SimpleRepository<UserEntity, Long> {

    UserEntity findByLicensePlateNumber(String licensePlateNumber);
}
