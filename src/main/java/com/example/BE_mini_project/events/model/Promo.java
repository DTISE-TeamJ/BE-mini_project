package com.example.BE_mini_project.events.model;

import com.example.BE_mini_project.authentication.model.Users;
import com.example.BE_mini_project.transaction.model.OrderItem;
import com.example.BE_mini_project.transaction.model.PromoUsage;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "promo")
public class Promo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "promo_id_gen")
    @SequenceGenerator(name = "promo_id_gen", sequenceName = "promo_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "promo_type")
    private PromoType promoType;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "start_valid")
    private LocalDateTime startValid;

    @Column(name = "end_valid")
    private LocalDateTime endValid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Events event;

    @Column(name = "promo_code", unique = true)
    private String promoCode;

    @Column(name = "usage_count")
    private Integer usageCount = 0;

    @OneToMany(mappedBy = "promo")
    private List<PromoUsage> promoUsages = new ArrayList<>();

    @OneToMany(mappedBy = "appliedPromo")
    private List<OrderItem> appliedOrderItems = new ArrayList<>();

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean isAvailable() {
        return usageCount < quantity
                && LocalDateTime.now().isAfter(startValid)
                && LocalDateTime.now().isBefore(endValid);
    }

    public void incrementUsageCount() {
        this.usageCount++;
    }

}