package com.example.BE_mini_project.events.service;

import com.example.BE_mini_project.events.dto.CreateEventDTO;
import com.example.BE_mini_project.events.dto.EventsDTO;
import com.example.BE_mini_project.events.model.Events;
import com.example.BE_mini_project.events.repository.EventCategoryRepository;
import com.example.BE_mini_project.events.repository.EventRepository;
import com.example.BE_mini_project.authentication.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    public EventsDTO createEvent(CreateEventDTO eventCreateDTO) {
        Events event = new Events();
        event.setName(eventCreateDTO.getName());
        event.setDate(Timestamp.valueOf(eventCreateDTO.getDate()));
        event.setStart(LocalTime.parse(eventCreateDTO.getStart()));
        event.setEnd(LocalTime.parse(eventCreateDTO.getEnd()));
        event.setPic(eventCreateDTO.getPic());
        event.setOrganization(eventCreateDTO.getOrganization());
        event.setLocation(eventCreateDTO.getLocation());
        event.setDescription(eventCreateDTO.getDescription());
        event.setIsFree(eventCreateDTO.isFree());

        Events createdEvent = eventRepository.save(event);

        return new EventsDTO(createdEvent);
    }

    public List<EventsDTO> getAllEvents() {
        List<Events> events = eventRepository.findAll();
        return events.stream()
                .map(EventsDTO::new)
                .collect(Collectors.toList());
    }

//    public List<EventsDTO> getEventsByLocation(String location) {
//        List<Events> events = eventRepository.findByLocation(location);
//        return events.stream().map(EventsDTO::new).collect(Collectors.toList());
//    }

//    public List<EventsDTO> getEventsByDateRange(Timestamp startDate, Timestamp endDate) {
//        List<Events> events = eventRepository.findByDateBetween(startDate, endDate);
//        return events.stream().map(EventsDTO::new).collect(Collectors.toList());
//    }
//
//    public List<EventsDTO> getEventsByOrganization(String organization) {
//        List<Events> events = eventRepository.findByOrganization(organization);
//        return events.stream().map(EventsDTO::new).collect(Collectors.toList());
//    }
//
//    public List<EventsDTO> getEventsByLocationAndOrganization(String location, String organization) {
//        List<Events> events = eventRepository.findByLocationAndOrganization(location, organization);
//        return events.stream().map(EventsDTO::new).collect(Collectors.toList());
//    }
//
//    public List<EventsDTO> getEventsByLocationAndDateRange(String location, Timestamp startDate, Timestamp endDate) {
//        List<Events> events = eventRepository.findByLocationAndDateBetween(location, startDate, endDate);
//        return events.stream().map(EventsDTO::new).collect(Collectors.toList());
//    }
//
//    public List<EventsDTO> getEventsByOrganizationAndDateRange(String organization, Timestamp startDate, Timestamp endDate) {
//        List<Events> events = eventRepository.findByOrganizationAndDateBetween(organization, startDate, endDate);
//        return events.stream().map(EventsDTO::new).collect(Collectors.toList());
//    }
//
//    public List<EventsDTO> getEventsByLocationOrganizationAndDateRange(String location, String organization, Timestamp startDate, Timestamp endDate) {
//        List<Events> events = eventRepository.findByLocationAndOrganizationAndDateBetween(location, organization, startDate, endDate);
//        return events.stream().map(EventsDTO::new).collect(Collectors.toList());
//    }

    public List<EventsDTO> getEventsByFilters(String location, String organization, LocalDateTime startDate, LocalDateTime endDate) {
        Timestamp startTimestamp = startDate != null ? Timestamp.valueOf(startDate) : null;
        Timestamp endTimestamp = endDate != null ? Timestamp.valueOf(endDate) : null;

        List<Events> events = eventRepository.findByFilters(location, organization, startTimestamp, endTimestamp);
        return events.stream().map(EventsDTO::new).collect(Collectors.toList());
    }

}