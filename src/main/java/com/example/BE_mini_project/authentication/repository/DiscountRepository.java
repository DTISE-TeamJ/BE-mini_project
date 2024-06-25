package com.example.BE_mini_project.authentication.repository;

import com.example.BE_mini_project.authentication.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findAllByExpiredAtBefore(Timestamp now);
}
