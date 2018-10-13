package com.parkingsystem.infrastructure.repository;


import com.parkingsystem.domain.model.management.ParkingLotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotSimpleRepository extends JpaRepository<ParkingLotEntity, Long> {

    ParkingLotEntity findFirstByAddress(String address);
}