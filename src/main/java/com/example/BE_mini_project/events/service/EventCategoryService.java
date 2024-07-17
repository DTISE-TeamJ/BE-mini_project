package com.example.BE_mini_project.events.service;

import com.example.BE_mini_project.events.dto.EventCategoryDTO;
import com.example.BE_mini_project.events.mapper.EventCategoryMapper;
import com.example.BE_mini_project.events.model.EventCategory;
import com.example.BE_mini_project.events.repository.EventCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventCategoryService {

    private final EventCategoryRepository eventCategoryRepository;
    private final EventCategoryMapper eventCategoryMapper;

    public EventCategoryService(EventCategoryRepository eventCategoryRepository, EventCategoryMapper eventCategoryMapper) {
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventCategoryMapper = eventCategoryMapper;
    }

    public List<EventCategoryDTO> getAllEventCategories() {
        List<EventCategory> categories = eventCategoryRepository.findAll();
        return eventCategoryMapper.toDTOList(categories);
    }
}
