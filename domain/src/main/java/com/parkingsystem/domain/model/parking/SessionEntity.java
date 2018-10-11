package com.parkingsystem.domain.model.parking;

import com.parkingsystem.domain.model.management.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SESSION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "STARTED_AT", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime startedAt;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private UserEntity user;

    @Column(name = "ENDED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATE_INTERVAL", nullable = false, updatable = false)
    private Integer updateInterval;

    @Column(name = "RATE", nullable = false, updatable = false)
    private BigDecimal rate;

    @Column(name = "LICENSE_PLATE_NUMBER", nullable = false, updatable = false)
    private String licensePlateNumber;

}
