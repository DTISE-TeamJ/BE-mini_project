package com.example.BE_mini_project.events.controller;

import com.example.BE_mini_project.events.dto.CreateEventDTO;
import com.example.BE_mini_project.events.dto.EventsDTO;
import com.example.BE_mini_project.events.dto.UpdateEventDTO;
import com.example.BE_mini_project.events.service.EventService;
import com.example.BE_mini_project.response.CustomResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
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

    @GetMapping
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

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<EventsDTO>> getEventById(@PathVariable Long id) {
        EventsDTO eventDTO = eventService.getEventById(id);

        CustomResponse<EventsDTO> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "Event Detail fetched successfully",
                eventDTO
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

    @GetMapping("/search")
    public ResponseEntity<CustomResponse<List<EventsDTO>>> searchEvents(
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "organization", required = false) String organization,
            @RequestParam(value = "description", required = false) String description) {

        List<EventsDTO> events = eventService.searchEvents(location, name, organization, description);

        CustomResponse<List<EventsDTO>> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "Events fetched successfully",
                events
        );

        return customResponse.toResponseEntity();
    }

}
