package com.example.BE_mini_project.events.repository;

import com.example.BE_mini_project.events.model.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
    @Query("SELECT ec.name FROM EventCategory ec")
    List<String> findAllCategoryNames();
}