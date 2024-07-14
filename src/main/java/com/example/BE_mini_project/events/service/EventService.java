package com.example.BE_mini_project.events.service;

import com.example.BE_mini_project.authentication.model.Users;
import com.example.BE_mini_project.events.dto.CreateEventDTO;
import com.example.BE_mini_project.events.dto.EventsDTO;
import com.example.BE_mini_project.events.dto.TicketTypeDTO;
import com.example.BE_mini_project.events.dto.UpdateEventDTO;
import com.example.BE_mini_project.events.mapper.EventMapper;
import com.example.BE_mini_project.events.model.EventCategory;
import com.example.BE_mini_project.events.model.Events;
import com.example.BE_mini_project.events.model.Promo;
import com.example.BE_mini_project.events.model.TicketType;
import com.example.BE_mini_project.events.repository.EventCategoryRepository;
import com.example.BE_mini_project.events.repository.EventRepository;
import com.example.BE_mini_project.authentication.repository.UsersRepository;
import com.example.BE_mini_project.events.repository.EventSpecification;
import jakarta.transaction.Transactional;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class EventService {
    private final EventRepository eventRepository;
    private final UsersRepository usersRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final CloudinaryService cloudinaryService;
    private final TicketService ticketService;
    private final PromoService promoService;
    private final EventMapper eventMapper;

    public EventService (EventRepository eventRepository, UsersRepository usersRepository, EventCategoryRepository eventCategoryRepository, CloudinaryService cloudinaryService, TicketService ticketService, PromoService promoService, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.usersRepository = usersRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.cloudinaryService = cloudinaryService;
        this.ticketService = ticketService;
        this.promoService = promoService;
        this.eventMapper = eventMapper;
    }

    @Transactional
    public EventsDTO createEvent(MultipartFile file, CreateEventDTO createEventDTO) {
        // Cloudinary upload logic
        Map<String, String> uploadResult = cloudinaryService.uploadFile(file);
        String imageUrl = uploadResult.get("url");

        // Create event
        Events event = new Events();
        event.setName(createEventDTO.getName());
        event.setStart(LocalDateTime.parse(createEventDTO.getStart()));
        event.setEnd(LocalDateTime.parse(createEventDTO.getEnd()));
        event.setPic(imageUrl);
        event.setOrganization(createEventDTO.getOrganization());
        event.setLocation(createEventDTO.getLocation());
        event.setDescription(createEventDTO.getDescription());

        // Fetch user and event category
        Users user = usersRepository.findById(createEventDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        event.setUser(user);

        EventCategory eventCategory = eventCategoryRepository.findById(createEventDTO.getEventCategoryId())
                .orElseThrow(() -> new RuntimeException("Event category not found"));
        event.setEventCategory(eventCategory);

        Events createdEvent = eventRepository.save(event);

        List<TicketType> ticketTypes = ticketService.createTicketTypes(createEventDTO.getTicketTypes(), createdEvent);
        event.setTicketTypes(ticketTypes);

        if (createEventDTO.getPromos() != null && !createEventDTO.getPromos().isEmpty()) {
            List<Promo> promos = promoService.createPromos(createEventDTO.getPromos(), createdEvent);
            createdEvent.getPromos().addAll(promos);
        }

        createdEvent = eventRepository.save(createdEvent);


        return new EventsDTO(createdEvent);
    }

    public Page<EventsDTO> getAllEvents(Pageable pageable) {
        Page<Events> eventsPage = eventRepository.findAll(pageable);
        return eventsPage.map(eventMapper::toDTO);
    }

    public EventsDTO getEventById(Long id) {
        return eventRepository.findById(id)
                .map(event -> new EventsDTO(event)) // Assuming you have a constructor in EventsDTO that accepts an Event entity
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
    }

    public EventsDTO updateEvent(Long id, MultipartFile file, UpdateEventDTO updateEventDTO) {
        // Fetch the existing event
        Events event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));

        // Cloudinary upload logic if file is not null
        if (file != null && !file.isEmpty()) {
            Map<String, String> uploadResult = cloudinaryService.uploadFile(file);
            String imageUrl = uploadResult.get("url");
            event.setPic(imageUrl);
        }

        // Update event details
        if (updateEventDTO.getName() != null && !updateEventDTO.getName().trim().isEmpty()) {
            event.setName(updateEventDTO.getName());
        }
        if (updateEventDTO.getStart() != null && !updateEventDTO.getStart().trim().isEmpty()) {
            event.setStart(LocalDateTime.parse(updateEventDTO.getStart()));
        }
        if (updateEventDTO.getEnd() != null && !updateEventDTO.getEnd().trim().isEmpty()) {
            event.setEnd(LocalDateTime.parse(updateEventDTO.getEnd()));
        }
        if (updateEventDTO.getOrganization() != null && !updateEventDTO.getOrganization().trim().isEmpty()) {
            event.setOrganization(updateEventDTO.getOrganization());
        }
        if (updateEventDTO.getLocation() != null && !updateEventDTO.getLocation().trim().isEmpty()) {
            event.setLocation(updateEventDTO.getLocation());
        }
        if (updateEventDTO.getDescription() != null && !updateEventDTO.getDescription().trim().isEmpty()) {
            event.setDescription(updateEventDTO.getDescription());
        }

        // Fetch user and event category
        if (updateEventDTO.getUserId() != null) {
            Users user = usersRepository.findById(updateEventDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            event.setUser(user);
        }

        if (updateEventDTO.getEventCategoryId() != null) {
            EventCategory eventCategory = eventCategoryRepository.findById(updateEventDTO.getEventCategoryId())
                    .orElseThrow(() -> new RuntimeException("Event category not found"));
            event.setEventCategory(eventCategory);
        }

        // Save updated event
        Events updatedEvent = eventRepository.save(event);

        return new EventsDTO(updatedEvent);
    }

    public EventsDTO deleteEvent(Long id) {
        Optional<Events> optionalEvent = eventRepository.findById(id);

        if (optionalEvent.isPresent()) {
            eventRepository.deleteById(id);
            return new EventsDTO(optionalEvent.get());
        } else {
            throw new ResourceNotFoundException("Event not found with id " + id);
        }
    }

    public Page<EventsDTO> searchEvents(String keyword, String categoryName, String location,
                                        LocalDateTime startDate, LocalDateTime endDate,
                                        Double minPrice, Double maxPrice,
                                        Pageable pageable) {
        Specification<Events> spec = Specification.where(EventSpecification.isNotDeleted())
                .and(EventSpecification.searchByKeyword(keyword))
                .and(EventSpecification.hasCategory(categoryName))
                .and(EventSpecification.hasLocation(location))
                .and(EventSpecification.dateRangeBetween(startDate, endDate))
                .and(EventSpecification.priceRangeBetween(minPrice, maxPrice));

        Sort sort = pageable.getSort();
        Sort.Order priceOrder = sort.getOrderFor("price");
        List<Sort.Order> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            if (!order.getProperty().equals("price")) {
                orders.add(order);
            }
        }

        if (orders.isEmpty()) {
            orders.add(Sort.Order.asc("id"));
        }

        Sort newSort = Sort.by(orders);

        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), newSort);

        Page<Events> eventsPage = eventRepository.findAll(spec, newPageable);

        List<EventsDTO> eventsDTOs = eventsPage.getContent().stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());

        if (priceOrder != null) {
            eventsDTOs.sort((e1, e2) -> {
                double price1 = priceOrder.getDirection() == Sort.Direction.ASC ?
                        getMinPrice(e1) : getMaxPrice(e1);
                double price2 = priceOrder.getDirection() == Sort.Direction.ASC ?
                        getMinPrice(e2) : getMaxPrice(e2);
                return priceOrder.getDirection() == Sort.Direction.ASC ?
                        Double.compare(price1, price2) : Double.compare(price2, price1);
            });
        }

        return new PageImpl<>(eventsDTOs, pageable, eventsPage.getTotalElements());
    }

    private double getMinPrice(EventsDTO event) {
        return event.getTicketTypes().stream()
                .mapToDouble(TicketTypeDTO::getPrice)
                .min()
                .orElse(Double.MAX_VALUE);
    }

    private double getMaxPrice(EventsDTO event) {
        return event.getTicketTypes().stream()
                .mapToDouble(TicketTypeDTO::getPrice)
                .max()
                .orElse(0.0);
    }
}