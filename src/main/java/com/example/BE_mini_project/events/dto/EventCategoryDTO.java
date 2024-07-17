package com.example.BE_mini_project.events.dto;

import com.example.BE_mini_project.events.model.EventCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventCategoryDTO {
    private Long id;
    private String name;

    public EventCategoryDTO(EventCategory eventCategory) {
        this.id = eventCategory.getId();
        this.name = eventCategory.getName();
    }
}

