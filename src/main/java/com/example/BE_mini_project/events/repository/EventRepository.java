package com.example.BE_mini_project.events.repository;

import com.example.BE_mini_project.events.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Events, Long> {
    @Query("SELECT e FROM Events e " +
            "WHERE (:location IS NULL OR e.location LIKE %:location%) " +
            "AND (:name IS NULL OR e.name LIKE %:name%) " +
            "AND (:organization IS NULL OR e.organization LIKE %:organization%) " +
            "AND (:description IS NULL OR e.description LIKE %:description%)")
    List<Events> findByCriteria(String location, String name, String organization, String description);
}
