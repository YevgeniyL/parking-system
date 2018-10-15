package com.parkingsystem.domain.model.parking;

import com.parkingsystem.domain.model.management.ParkingLotEntity;
import com.parkingsystem.domain.model.management.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SESSION")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "STARTED_AT", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime startedAt;

    @Column(name = "ENDED_AT")
    private LocalDateTime endedAt;

    @Column(name = "ROUND_INTERVAL", nullable = false, updatable = false)
    private Integer roundInterval;

    @Column(name = "TARIFF", nullable = false, updatable = false)
    private BigDecimal tariff;

    @Column(name = "USER_BALANCE", nullable = false, updatable = false)
    private BigDecimal userBalance;

    @Column(name = "MINIMAL_AMOUNT", nullable = false, updatable = false)
    private BigDecimal minimalAmount;

    @Column(name = "MINIMAL_CREDIT_AMOUNT", nullable = false, updatable = false)
    private BigDecimal minimalCreditAmount;

    @Column(name = "LICENSE_PLATE_NUMBER", nullable = false, updatable = false)
    private String licensePlateNumber;

    @Column(name = "TOTAL_COST")
    private BigDecimal totalCost;

    @ManyToOne
    @JoinColumn(name = "PARKING_LOT_ID", referencedColumnName = "ID")
    private ParkingLotEntity parkingLot;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private UserEntity user;

    public SessionEntity(UserEntity user, ParkingLotEntity parkingLot, Integer roundInterval, BigDecimal tariff, BigDecimal userBalance, BigDecimal minimalAmount, BigDecimal minimalAmountForCredit, String licensePlateNumber) {
        this.user = user;
        this.parkingLot = parkingLot;
        this.roundInterval = roundInterval;
        this.tariff = tariff;
        this.userBalance = userBalance;
        this.minimalAmount = minimalAmount;
        this.minimalCreditAmount = minimalAmountForCredit;
        this.licensePlateNumber = licensePlateNumber;
    }
}
