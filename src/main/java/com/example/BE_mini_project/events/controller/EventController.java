package com.example.BE_mini_project.events.controller;

import com.example.BE_mini_project.events.dto.CreateEventDTO;
import com.example.BE_mini_project.events.dto.EventsDTO;
import com.example.BE_mini_project.events.service.EventService;
import com.example.BE_mini_project.response.CustomResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

@RestController
@RequestMapping("/api/v1")
public class EventController {
    private final EventService eventService;

    public EventController (EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create-event")
    public ResponseEntity<CustomResponse<EventsDTO>> createEvent(@RequestBody CreateEventDTO createEventDTO) {
        EventsDTO createdEventDTO = eventService.createEvent(createEventDTO);

        // Create a CustomResponse object
        CustomResponse<EventsDTO> customResponse = new CustomResponse<>(
                HttpStatus.CREATED,
                "Created",
                "Event created successfully",
                createdEventDTO
        );

        // Return ResponseEntity
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

//    @GetMapping("/events-location")
//    public ResponseEntity<CustomResponse<List<EventsDTO>>> getEventsByLocation(@RequestParam String location) {
//        List<EventsDTO> eventDTOs = eventService.getEventsByLocation(location);
//
//        CustomResponse<List<EventsDTO>> customResponse = new CustomResponse<>(
//                HttpStatus.OK,
//                "Success",
//                "Events retrieved successfully by location",
//                eventDTOs
//        );
//
//        return customResponse.toResponseEntity();
//    }

//    @GetMapping("/events-organization")
//    public ResponseEntity<CustomResponse<List<EventsDTO>>> getEventsByOrganization(@RequestParam String organization) {
//        List<EventsDTO> eventDTOs = eventService.getEventsByOrganization(organization);
//
//        CustomResponse<List<EventsDTO>> customResponse = new CustomResponse<>(
//                HttpStatus.OK,
//                "Success",
//                "Events retrieved successfully by organization",
//                eventDTOs
//        );
//
//        return customResponse.toResponseEntity();
//    }
//
//    @GetMapping("/events-date-range")
//    public ResponseEntity<CustomResponse<List<EventsDTO>>> getEventsByDateRange(
//            @RequestParam Timestamp startDate, @RequestParam Timestamp endDate) {
//        List<EventsDTO> eventDTOs = eventService.getEventsByDateRange(startDate, endDate);
//
//        CustomResponse<List<EventsDTO>> customResponse = new CustomResponse<>(
//                HttpStatus.OK,
//                "Success",
//                "Events retrieved successfully by date range",
//                eventDTOs
//        );
//
//        return customResponse.toResponseEntity();
//    }
//
//    @GetMapping("/events-location-and-organization")
//    public ResponseEntity<CustomResponse<List<EventsDTO>>> getEventsByLocationAndOrganization(
//            @RequestParam String location, @RequestParam String organization) {
//        List<EventsDTO> eventDTOs = eventService.getEventsByLocationAndOrganization(location, organization);
//
//        CustomResponse<List<EventsDTO>> customResponse = new CustomResponse<>(
//                HttpStatus.OK,
//                "Success",
//                "Events retrieved successfully by location and organization",
//                eventDTOs
//        );
//
//        return customResponse.toResponseEntity();
//    }
//
//    @GetMapping("/events-location-and-date-range")
//    public ResponseEntity<CustomResponse<List<EventsDTO>>> getEventsByLocationAndDateRange(
//            @RequestParam String location, @RequestParam Timestamp startDate, @RequestParam Timestamp endDate) {
//        List<EventsDTO> eventDTOs = eventService.getEventsByLocationAndDateRange(location, startDate, endDate);
//
//        CustomResponse<List<EventsDTO>> customResponse = new CustomResponse<>(
//                HttpStatus.OK,
//                "Success",
//                "Events retrieved successfully by location and date range",
//                eventDTOs
//        );
//
//        return customResponse.toResponseEntity();
//    }
//
//    @GetMapping("/events-organization-and-date-range")
//    public ResponseEntity<CustomResponse<List<EventsDTO>>> getEventsByOrganizationAndDateRange(
//            @RequestParam String organization, @RequestParam Timestamp startDate, @RequestParam Timestamp endDate) {
//        List<EventsDTO> eventDTOs = eventService.getEventsByOrganizationAndDateRange(organization, startDate, endDate);
//
//        CustomResponse<List<EventsDTO>> customResponse = new CustomResponse<>(
//                HttpStatus.OK,
//                "Success",
//                "Events retrieved successfully by organization and date range",
//                eventDTOs
//        );
//
//        return customResponse.toResponseEntity();
//    }
//
//    @GetMapping("/events-location-organization-and-date-range")
//    public ResponseEntity<CustomResponse<List<EventsDTO>>> getEventsByLocationOrganizationAndDateRange(
//            @RequestParam String location, @RequestParam String organization,
//            @RequestParam Timestamp startDate, @RequestParam Timestamp endDate) {
//        List<EventsDTO> eventDTOs = eventService.getEventsByLocationOrganizationAndDateRange(
//                location, organization, startDate, endDate);
//
//        CustomResponse<List<EventsDTO>> customResponse = new CustomResponse<>(
//                HttpStatus.OK,
//                "Success",
//                "Events retrieved successfully by location, organization, and date range",
//                eventDTOs
//        );
//
//        return customResponse.toResponseEntity();
//    }

//    @GetMapping("/all-events")
//    public ResponseEntity<CustomResponse<List<EventsDTO>>> getEventsByFilters(
//            @RequestParam(required = false) String location,
//            @RequestParam(required = false) String organization,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
//
//        List<EventsDTO> events;
//
//        if (location == null && organization == null && startDate == null && endDate == null) {
//            events = eventService.getAllEvents();
//        } else {
//            events = eventService.getEventsByFilters(location, organization, startDate, endDate);
//        }
//
//        CustomResponse<List<EventsDTO>> customResponse = new CustomResponse<>(
//                HttpStatus.OK,
//                "Success",
//                "Events retrieved successfully",
//                events
//        );
//
//        return customResponse.toResponseEntity();
//    }
}
