package com.example.BE_mini_project.transaction.repository;

import com.example.BE_mini_project.transaction.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
