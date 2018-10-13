package com.parkingsystem.infrastructure.repository;

import com.parkingsystem.domain.model.management.UserEntity;
import com.parkingsystem.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private UserSimpleRepository userSimpleRepository;

    @Override
    @Transactional(readOnly = true)
    public UserEntity find(String licensePlateNumber) {
        return userSimpleRepository.findByLicensePlateNumber(licensePlateNumber);
    }

    @Override
    @Transactional
    public void save(UserEntity userEntity) {
        userSimpleRepository.save(userEntity);
    }
}
