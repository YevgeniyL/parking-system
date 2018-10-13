package com.parkingsystem.domain.repository;

import com.parkingsystem.domain.model.parking.SessionEntity;

public interface SessionRepository {

    SessionEntity findLastWhereEndedIsNullBy(String licensePlateNumber);

    void save(SessionEntity sessionEntity);
}
