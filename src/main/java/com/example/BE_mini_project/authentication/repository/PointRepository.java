package com.example.BE_mini_project.authentication.repository;

import com.example.BE_mini_project.authentication.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findAllByExpiredAtBefore(Timestamp now);
}