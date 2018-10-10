package com.parkingsystem.infrastructure.repository;

import com.parkingsystem.domain.model.management.ParkingLotEntity;
import com.parkingsystem.domain.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ParkingLotRepositoryImpl implements ParkingLotRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private ParkingLotSimpleRepository simpleRepository;

    @Override
    @Transactional
    public void save(ParkingLotEntity parkingLotEntity) throws DataIntegrityViolationException {
        simpleRepository.save(parkingLotEntity);
    }

}