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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @Column(name = "expired_at")
    private Timestamp expiredAt;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public Discount() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = Timestamp.valueOf(now);

        LocalDateTime expiryDateTime = now.plusMonths(3);
        expiryDateTime = expiryDateTime.withHour(23).withMinute(59).withSecond(59).withNano(9999999);

        this.expiredAt = Timestamp.valueOf(expiryDateTime);
        this.hasDiscount = false;
    }
}