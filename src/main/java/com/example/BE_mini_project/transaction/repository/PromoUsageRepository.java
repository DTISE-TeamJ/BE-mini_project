package com.example.BE_mini_project.transaction.repository;

import com.example.BE_mini_project.transaction.model.PromoUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoUsageRepository extends JpaRepository<PromoUsage, Long> {
    long countByPromoId(Long promoId);
}
