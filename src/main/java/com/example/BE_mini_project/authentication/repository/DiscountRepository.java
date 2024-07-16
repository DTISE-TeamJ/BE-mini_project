package com.example.BE_mini_project.authentication.repository;

import com.example.BE_mini_project.authentication.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findAllByExpiredAtBefore(Timestamp now);

    @Modifying
    @Transactional
    @Query("UPDATE Discount d SET d.hasDiscount = false WHERE d.expiredAt <= :now")
    void updateExpiredDiscounts(@Param("now") LocalDateTime now);
}
