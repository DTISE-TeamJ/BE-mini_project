package com.example.BE_mini_project.events.repository;

import com.example.BE_mini_project.events.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Events, Long>, JpaSpecificationExecutor<Events> {

    @Query("SELECT DISTINCT e.location FROM Events e")
    List<String> findDistinctLocations();

    List<Events> findByUserId(Long userId);


}
