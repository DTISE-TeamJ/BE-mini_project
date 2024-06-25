package com.example.BE_mini_project.events.dto;

import com.example.BE_mini_project.authentication.dto.UserDTO;
import com.example.BE_mini_project.events.model.EventCategory;
import com.example.BE_mini_project.events.model.Events;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventsDTO {
    private Long id;
    private String name;
    private LocalDateTime date;
    private String start;
    private String end;
    private String pic;
    private String organization;
    private String location;
    private String description;
    private boolean isFree;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
//    private EventCategory eventCategory;
    private EventCategoryDTO eventCategory;
    private UserDTO user;


    // Constructor to map from Event entity
    public EventsDTO(Events event) {
        this.id = event.getId();
        this.name = event.getName();
        this.date = event.getDate() != null ? event.getDate().toLocalDateTime() : null;
        this.start = event.getStart() != null ? String.valueOf(event.getStart()) : null;
        this.end = event.getEnd() != null ? String.valueOf(event.getEnd()) : null;
        this.pic = event.getPic();
        this.organization = event.getOrganization();
        this.location = event.getLocation();
        this.description = event.getDescription();
        this.isFree = event.getIsFree();
        this.createdAt = event.getCreatedAt() != null ? event.getCreatedAt().toLocalDateTime() : null;
        this.updatedAt = event.getUpdatedAt() != null ? event.getUpdatedAt().toLocalDateTime() : null;
        this.deletedAt = event.getDeletedAt() != null ? event.getDeletedAt().toLocalDateTime() : null;
//        this.eventCategory = event.getEventCategory();
        this.eventCategory = new EventCategoryDTO(event.getEventCategory());
        this.user = new UserDTO(event.getUser());
    }
}