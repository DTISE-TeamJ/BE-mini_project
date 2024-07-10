package com.example.BE_mini_project.events.dto;

import com.example.BE_mini_project.authentication.dto.UserDTO;
import com.example.BE_mini_project.events.model.Events;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class EventsDTO {
    private Long id;
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
    private String pic;
    private String organization;
    private String location;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private EventCategoryDTO eventCategory;
    private UserDTO user;
    private List<TicketTypeDTO> ticketTypes;
    private List<PromoDTO> promos;


    // Constructor to map from Event entity
    public EventsDTO(Events event) {
        this.id = event.getId();
        this.name = event.getName();
        this.start = event.getStart() != null ? LocalDateTime.parse(String.valueOf(event.getStart())) : null;
        this.end = event.getEnd() != null ? LocalDateTime.parse(String.valueOf(event.getEnd())) : null;
        this.pic = event.getPic();
        this.organization = event.getOrganization();
        this.location = event.getLocation();
        this.description = event.getDescription();
        this.createdAt = event.getCreatedAt() != null ? event.getCreatedAt() : null;
        this.updatedAt = event.getUpdatedAt() != null ? event.getUpdatedAt() : null;
        this.deletedAt = event.getDeletedAt() != null ? event.getDeletedAt() : null;
        this.eventCategory = new EventCategoryDTO(event.getEventCategory());
        this.user = new UserDTO(event.getUser());
        this.ticketTypes = event.getTicketTypes() != null
                ? event.getTicketTypes().stream().map(TicketTypeDTO::new).collect(Collectors.toList())
                : null;
        this.promos = event.getPromos() != null
                ? event.getPromos().stream().map(PromoDTO::new).collect(Collectors.toList())
                : null;
    }
}