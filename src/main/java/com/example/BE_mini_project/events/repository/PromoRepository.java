package com.example.BE_mini_project.events.repository;

import com.example.BE_mini_project.events.model.Promo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromoRepository extends JpaRepository<Promo, Long> {
    List<Promo> findByEventId(Long eventId);
    Optional<Promo> findByPromoCode(String promoCode);
    Optional<Promo> findByPromoCodeAndEventId(String promoCode, Long eventId);
}
