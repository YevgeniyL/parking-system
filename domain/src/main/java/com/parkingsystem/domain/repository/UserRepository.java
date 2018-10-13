package com.parkingsystem.domain.repository;

import com.parkingsystem.domain.model.management.UserEntity;

public interface UserRepository {
    UserEntity find(String licensePlateNumber);

    void save(UserEntity userEntity);
}
