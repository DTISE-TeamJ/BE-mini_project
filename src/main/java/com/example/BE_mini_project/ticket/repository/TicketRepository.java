package com.example.BE_mini_project.ticket.repository;

import com.example.BE_mini_project.ticket.model.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.BE_mini_project.events.model.Events;
import java.util.List;
import java.util.Set;

public interface TicketRepository extends JpaRepository<TicketType, Long> {
//    List<TicketType> findByEvent(Events event);
//    Set<TicketType> findByEvents(Events events);
}