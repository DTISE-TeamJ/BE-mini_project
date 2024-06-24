package com.example.BE_mini_project.authentication.model;

import lombok.Data;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "point")
@Data
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invitee_id", nullable = false)
    private Users invitee;

    @ManyToOne
    @JoinColumn(name = "inviter_id", nullable = false)
    private Users inviter;

    @Column(name = "points", nullable = false)
    private int points;

    @Column(name = "expired_at", nullable = false)
    private Timestamp expiredAt;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public Point() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = Timestamp.valueOf(now);
        this.expiredAt = Timestamp.valueOf(now.plusMonths(3));
    }
}