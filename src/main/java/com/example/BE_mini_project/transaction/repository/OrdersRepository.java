package com.example.BE_mini_project.transaction.repository;

import com.example.BE_mini_project.authentication.model.Users;
import com.example.BE_mini_project.transaction.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query("SELECT o FROM Orders o WHERE o.user = :user AND o.isPaid = false")
    Optional<Orders> findUnpaidOrderByUser(@Param("user")Users user);

    List<Orders> findByUserIdAndIsPaidFalse(Long userId);
    List<Orders> findByUserIdAndIsPaidTrue(Long userId);
}
