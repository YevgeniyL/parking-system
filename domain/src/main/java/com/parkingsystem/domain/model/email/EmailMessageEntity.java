package com.parkingsystem.domain.model.email;

import com.parkingsystem.domain.model.management.UserEntity;
import com.parkingsystem.domain.model.parking.SessionEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "EMAIL_MESSAGE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SUBJECT", nullable = false, updatable = false)
    private String subject;

    @Column(name = "SEND_TO", nullable = false, updatable = false)
    private String sendTo;

    @Column(name = "CREATED", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime created;

    @Column(name = "SENDED")
    private LocalDateTime sended;

    @OneToOne
    @JoinColumn(name = "SESSION_ID", referencedColumnName = "ID")
    private SessionEntity session;

    @Column(name = "TEXT", nullable = false, updatable = false)
    private String text;
}
