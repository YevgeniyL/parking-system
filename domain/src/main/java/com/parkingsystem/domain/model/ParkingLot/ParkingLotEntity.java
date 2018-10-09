package com.parkingsystem.domain.model.ParkingLot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ParkingLot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "URL", nullable = false, updatable = true)
    private String url;

    @Column(name = "IS_ENABLED", nullable = false, updatable = true)
    private Boolean isEnabled;

    @Column(name = "CREATED", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime created;

    public ParkingLotEntity(String url, Boolean isEnabled, LocalDateTime created) {
        this.url = url;
        this.isEnabled = isEnabled;
        this.created = created;
    }
}
