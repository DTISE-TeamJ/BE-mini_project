package com.example.BE_mini_project.events.mapper;

import com.example.BE_mini_project.events.dto.EventCategoryDTO;
import com.example.BE_mini_project.events.model.EventCategory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventCategoryMapper {

    public EventCategoryDTO toDTO(EventCategory eventCategory) {
        if (eventCategory == null) {
            return null;
        }

        EventCategoryDTO dto = new EventCategoryDTO();
        dto.setId(eventCategory.getId());
        dto.setName(eventCategory.getName());
        return dto;
    }

    public List<EventCategoryDTO> toDTOList(List<EventCategory> eventCategories) {
        if (eventCategories == null) {
            return Collections.emptyList();
        }

        return eventCategories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
