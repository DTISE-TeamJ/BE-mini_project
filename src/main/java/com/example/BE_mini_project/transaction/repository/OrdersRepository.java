package com.example.BE_mini_project.transaction.repository;

import com.example.BE_mini_project.authentication.model.Users;
import com.example.BE_mini_project.transaction.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query("SELECT o FROM Orders o WHERE o.user = :user AND o.isPaid = false")
    Optional<Orders> findUnpaidOrderByUser(@Param("user")Users user);

    List<Orders> findByUserIdAndIsPaidFalse(Long userId);
    List<Orders> findByUserIdAndIsPaidTrue(Long userId);

    @Query("SELECT o FROM Orders o JOIN o.orderItems oi JOIN oi.ticketType tt JOIN tt.event e " +
            "WHERE o.isPaid = true AND e.user.id = :userId AND o.paidAt BETWEEN :startDate AND :endDate")
    List<Orders> findPaidOrdersForCreatorBetweenDates(@Param("userId") Long userId,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);
}
