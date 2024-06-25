package com.example.BE_mini_project.events.service;

import com.example.BE_mini_project.events.dto.CreateEventDTO;
import com.example.BE_mini_project.events.dto.EventsDTO;
import com.example.BE_mini_project.events.model.EventCategory;
import com.example.BE_mini_project.events.model.Events;
import com.example.BE_mini_project.events.repository.EventCategoryRepository;
import com.example.BE_mini_project.events.repository.EventRepository;
import com.example.BE_mini_project.authentication.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.stereotype.Service;

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
    private final Cloudinary cloudinary;


    public EventService (EventRepository eventRepository, UsersRepository usersRepository, EventCategoryRepository eventCategoryRepository, Cloudinary cloudinary) {
        this.eventRepository = eventRepository;
        this.usersRepository = usersRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.cloudinary = cloudinary;
    }


    public EventsDTO createEvent(CreateEventDTO createEventDTO, MultipartFile image) throws IOException {
        // Upload image to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = (String) uploadResult.get("url");

        // Create event and set properties
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

        // Fetch and set EventCategory
        EventCategory eventCategory = eventCategoryRepository.findById(createEventDTO.getEventCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid event category ID"));
        event.setEventCategory(eventCategory);

        // Save the event
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