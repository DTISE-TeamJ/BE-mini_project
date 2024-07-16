package com.example.BE_mini_project.events.repository;

import com.example.BE_mini_project.events.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
        boolean existsByUserIdAndEventId(Long userId, Long eventId);
        List<Review> findByEventId(Long eventId);
        List<Review> findByUserId(Long userId);

    }