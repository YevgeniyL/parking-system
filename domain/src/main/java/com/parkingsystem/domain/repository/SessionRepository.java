package com.parkingsystem.domain.repository;

import com.parkingsystem.domain.model.management.UserEntity;
import com.parkingsystem.domain.model.parking.SessionEntity;

import java.util.Optional;

public interface SessionRepository {

    SessionEntity findNotClosedBy(String licensePlateNumber);

    void save(SessionEntity sessionEntity);

    SessionEntity findAnyOneByUser(UserEntity user);

    SessionEntity findStartedAndNotClosedSessionBy(String licencePlateNumber);

    Optional<SessionEntity> find(Long sessionId);
}
