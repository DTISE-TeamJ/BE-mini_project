package com.example.BE_mini_project.events.service;

import com.example.BE_mini_project.authentication.model.Users;
import com.example.BE_mini_project.events.dto.CreateEventDTO;
import com.example.BE_mini_project.events.dto.EventsDTO;
import com.example.BE_mini_project.events.model.EventCategory;
import com.example.BE_mini_project.events.model.Events;
import com.example.BE_mini_project.events.repository.EventCategoryRepository;
import com.example.BE_mini_project.events.repository.EventRepository;
import com.example.BE_mini_project.authentication.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
public class EventService {
    private EventRepository eventRepository;

    private UsersRepository usersRepository;

    private EventCategoryRepository eventCategoryRepository;
    private CloudinaryService cloudinaryService;


    public EventService (EventRepository eventRepository, UsersRepository usersRepository, EventCategoryRepository eventCategoryRepository, CloudinaryService cloudinaryService) {
        this.eventRepository = eventRepository;
        this.usersRepository = usersRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public EventsDTO createEvent(MultipartFile file, CreateEventDTO createEventDTO) {
        // Cloudinary upload logic
        Map<String, String> uploadResult = cloudinaryService.uploadFile(file);
        String imageUrl = uploadResult.get("url");

        // Create event
        Events event = new Events();
        event.setName(createEventDTO.getName());
        event.setDate(Timestamp.valueOf(createEventDTO.getDate()));
        event.setStart(LocalTime.parse(createEventDTO.getStart()));
        event.setEnd(LocalTime.parse(createEventDTO.getEnd()));
        event.setPic(imageUrl);
        event.setOrganization(createEventDTO.getOrganization());
        event.setLocation(createEventDTO.getLocation());
        event.setDescription(createEventDTO.getDescription());
        event.setIsFree(createEventDTO.isFree());

        // Fetch user and event category
        Users user = usersRepository.findById(createEventDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        event.setUser(user);

        EventCategory eventCategory = eventCategoryRepository.findById(createEventDTO.getEventCategoryId())
                .orElseThrow(() -> new RuntimeException("Event category not found"));
        event.setEventCategory(eventCategory);

        // Save event
        Events createdEvent = eventRepository.save(event);

        return new EventsDTO(createdEvent);
    }

    public List<EventsDTO> getAllEvents() {
        List<Events> events = eventRepository.findAll();
        return events.stream()
                .map(EventsDTO::new)
                .collect(Collectors.toList());
    }

    public List<EventsDTO> getEventsByFilters(String location, String organization, LocalDateTime startDate, LocalDateTime endDate) {
        Timestamp startTimestamp = startDate != null ? Timestamp.valueOf(startDate) : null;
        Timestamp endTimestamp = endDate != null ? Timestamp.valueOf(endDate) : null;

        List<Events> events = eventRepository.findByFilters(location, organization, startTimestamp, endTimestamp);
        return events.stream().map(EventsDTO::new).collect(Collectors.toList());
    }
}