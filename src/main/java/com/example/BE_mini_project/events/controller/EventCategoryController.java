package com.example.BE_mini_project.events.controller;

import com.example.BE_mini_project.events.dto.EventCategoryDTO;
import com.example.BE_mini_project.events.model.EventCategory;
import com.example.BE_mini_project.events.service.EventCategoryService;
import com.example.BE_mini_project.response.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/event-categories")
public class EventCategoryController {

    private final EventCategoryService eventCategoryService;

    public EventCategoryController(EventCategoryService eventCategoryService) {
        this.eventCategoryService = eventCategoryService;
    }

    @GetMapping
    public ResponseEntity<CustomResponse<List<EventCategoryDTO>>> getAllEventCategories() {
        List<EventCategoryDTO> categories = eventCategoryService.getAllEventCategories();

        CustomResponse<List<EventCategoryDTO>> response = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "Event categories retrieved successfully",
                categories
        );

        return ResponseEntity.ok(response);
    }
}
