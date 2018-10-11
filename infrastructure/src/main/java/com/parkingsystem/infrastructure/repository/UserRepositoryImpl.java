package com.parkingsystem.infrastructure.repository;

import com.parkingsystem.domain.model.management.UserEntity;
import com.parkingsystem.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private UserSimpleRepository userSimpleRepository;

    @Override
    public UserEntity find(String licensePlateNumber) {
        return userSimpleRepository.findByLicensePlateNumber(licensePlateNumber);
    }
}
