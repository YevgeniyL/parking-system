package com.parkingsystem.infrastructure.repository;

import com.parkingsystem.domain.model.management.UserEntity;
import com.parkingsystem.domain.model.parking.SessionEntity;
import com.parkingsystem.domain.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class SessionRepositoryImpl implements SessionRepository {
    @Autowired
    SessionSimpleRepository simpleRepository;

    @Override
    @Transactional(readOnly = true)
    public SessionEntity findNotClosedBy(String licensePlateNumber) {
        return simpleRepository.findFirstByLicensePlateNumberAndEndedAtIsNull(licensePlateNumber);
    }

    @Override
    @Transactional
    public void save(SessionEntity sessionEntity) {
        simpleRepository.save(sessionEntity);
    }

    @Override
    public SessionEntity findAnyOneByUser(UserEntity user) {
        return simpleRepository.findFirstByUser(user);

    }

    @Override
    public SessionEntity findStartedAndNotClosedSessionBy(String licencePlateNumber) {
        return simpleRepository.findFirstByLicensePlateNumberAndStartedAtIsNotNullAndEndedAtIsNull(licencePlateNumber);
    }

    @Override
    public Optional<SessionEntity> find(Long sessionId) {
        return simpleRepository.findById(sessionId);
    }
}
