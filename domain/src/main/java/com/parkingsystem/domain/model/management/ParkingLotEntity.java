package com.parkingsystem.domain.model.management;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PARKING_LOT")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ParkingLotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ADDRESS", nullable = false, updatable = true, unique = true)
    private String address;

    @Column(name = "IS_ENABLED", nullable = false, updatable = true)
    private Boolean isEnabled;

    @Column(name = "CREATED", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime created;

    public ParkingLotEntity(String address, Boolean isEnabled) {
        this.address = address;
        this.isEnabled = isEnabled;
    }
}
