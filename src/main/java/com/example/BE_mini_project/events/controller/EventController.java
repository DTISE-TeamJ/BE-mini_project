package com.example.BE_mini_project.events.controller;

import com.example.BE_mini_project.events.dto.CreateEventDTO;
import com.example.BE_mini_project.events.dto.EventsDTO;
import com.example.BE_mini_project.events.service.EventService;
import com.example.BE_mini_project.response.CustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

@RestController
@RequestMapping("/api/v1")
public class EventController {
    private final EventService eventService;
    private final ObjectMapper objectMapper;


    public EventController (EventService eventService, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.eventService = eventService;
    }


    /*
    @PostMapping(value = "/create-event")
    public ResponseEntity<CustomResponse<EventsDTO>> createEvent(@RequestParam("image") MultipartFile file,
                                                                 @RequestParam("eventData") @Valid String createEventDTO) {
//        EventsDTO createdEventDTO = eventService.createEvent(file, createEventDTO);
//
//        CustomResponse<EventsDTO> customResponse = new CustomResponse<>(
//                HttpStatus.CREATED,
//                "Created",
//                "Event created successfully",
//                createdEventDTO
//        );
//
//        return customResponse.toResponseEntity();
        log.info(file.getOriginalFilename());
//        TODO: Convert request JSON to DTO then parse to service
        log.info(createEventDTO);

        return ResponseEntity.ok().build();
    }
    */

    @PostMapping("/create-event")
    public ResponseEntity<CustomResponse<EventsDTO>> createEvent(
            @RequestParam("eventData") String eventData,
            @RequestParam("image") MultipartFile image) throws IOException {

        // Convert eventData from JSON to CreateEventDTO
        CreateEventDTO createEventDTO = objectMapper.readValue(eventData, CreateEventDTO.class);

        // Call the service to create the event
        EventsDTO createdEventDTO = eventService.createEvent(createEventDTO, image);

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
