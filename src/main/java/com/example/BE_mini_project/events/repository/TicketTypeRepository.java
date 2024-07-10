package com.example.BE_mini_project.events.repository;

import com.example.BE_mini_project.events.model.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
    @Query("SELECT t FROM TicketType t JOIN FETCH t.event WHERE t.event.id = :eventId")
    List<TicketType> findByEventId(Long eventId);
}
