package com.example.BE_mini_project.events.controller;

import com.example.BE_mini_project.events.dto.CreateEventDTO;
import com.example.BE_mini_project.events.dto.EventsDTO;
import com.example.BE_mini_project.events.dto.UpdateEventDTO;
import com.example.BE_mini_project.events.service.EventService;
import com.example.BE_mini_project.response.CustomResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EventController {
    private final EventService eventService;
    private final ObjectMapper objectMapper;


    public EventController (EventService eventService, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.eventService = eventService;
    }

    @PostMapping(value = "/create-event", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CustomResponse<EventsDTO>> createEvent(
            @RequestParam("image") MultipartFile file,
            @RequestParam("eventData") String eventData) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        CreateEventDTO createEventDTO = objectMapper.readValue(eventData, CreateEventDTO.class);

        EventsDTO createdEventDTO = eventService.createEvent(file, createEventDTO);

        CustomResponse<EventsDTO> customResponse = new CustomResponse<>(
                HttpStatus.CREATED,
                "Created",
                "Event created successfully",
                createdEventDTO
        );

        return customResponse.toResponseEntity();
    }

    @GetMapping("/all-events")
    public ResponseEntity<CustomResponse<List<EventsDTO>>> getAllEvents() {
        List<EventsDTO> eventDTOs = eventService.getAllEvents();

        CustomResponse<List<EventsDTO>> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "Events retrieved successfully",
                eventDTOs
        );

        return customResponse.toResponseEntity();
    }

    @PutMapping(value = "/edit-event/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CustomResponse<EventsDTO>> updateEvent(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile file,
            @RequestParam("eventData") String eventData) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        UpdateEventDTO updateEventDTO = objectMapper.readValue(eventData, UpdateEventDTO.class);

        EventsDTO updatedEventDTO = eventService.updateEvent(id, file, updateEventDTO);

        CustomResponse<EventsDTO> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Updated",
                "Event updated successfully",
                updatedEventDTO
        );

        return customResponse.toResponseEntity();
    }

    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<CustomResponse<EventsDTO>> deleteEvent(@PathVariable Long id) {
        EventsDTO deletedEvent = eventService.deleteEvent(id);

        CustomResponse<EventsDTO> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Deleted",
                "Event deleted successfully",
                deletedEvent
        );
        return customResponse.toResponseEntity();
    }

    @GetMapping("/search-events")
    public ResponseEntity<CustomResponse<List<EventsDTO>>> getEventsByFilters(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String organization,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<EventsDTO> events;

        if (location == null && organization == null && startDate == null && endDate == null) {
            events = eventService.getAllEvents();
        } else {
            events = eventService.getEventsByFilters(location, organization, startDate, endDate);
        }

        CustomResponse<List<EventsDTO>> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "Events retrieved successfully",
                events
        );

        return customResponse.toResponseEntity();
    }
}
