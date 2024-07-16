package com.example.BE_mini_project.authentication.repository;

import com.example.BE_mini_project.authentication.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findAllByExpiredAtBefore(Timestamp now);
    List<Point> findByInviterIdAndExpiredAtAfterOrderByExpiredAtAsc(Long inviterId, LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE Point p SET p.points = 0 WHERE p.expiredAt <= :now")
    void updateExpiredPoints(@Param("now") LocalDateTime now);

    @Query("SELECT COALESCE(SUM(p.points), 0) FROM Point p WHERE p.inviter.id = :userId")
    Integer sumPointsByUserId(@Param("userId") Long userId);
}