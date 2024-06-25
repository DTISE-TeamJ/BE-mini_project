package com.example.BE_mini_project.authentication.model;

import lombok.Data;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Entity
@Table(name = "discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "has_discount")
    @ColumnDefault("false")
    private boolean hasDiscount;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "expired_at")
    private Timestamp expiredAt;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public Discount() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = Timestamp.valueOf(now);
        this.expiredAt = Timestamp.valueOf(now.plusMonths(3));
        this.hasDiscount = false;
    }
}