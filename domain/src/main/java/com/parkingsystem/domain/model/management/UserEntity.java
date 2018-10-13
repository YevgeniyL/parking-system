package com.parkingsystem.domain.model.management;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMAIL", nullable = false, updatable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "BALANCE")
    private BigDecimal balance = BigDecimal.valueOf(0);

    @Column(name = "LICENSE_PLATE_NUMBER", unique = true)
    private String licensePlateNumber;

    @Column(name = "CREATED", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime created;

    public UserEntity(String email, String password, String licensePlateNumber) {
        this.email = email;
        this.password = password;
        this.licensePlateNumber = licensePlateNumber;
    }
}
