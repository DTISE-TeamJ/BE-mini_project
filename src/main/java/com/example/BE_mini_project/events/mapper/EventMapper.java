package com.example.BE_mini_project.events.mapper;

import com.example.BE_mini_project.authentication.dto.UserDTO;
import com.example.BE_mini_project.events.dto.EventCategoryDTO;
import com.example.BE_mini_project.events.dto.EventsDTO;
import com.example.BE_mini_project.events.model.Events;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    private final TicketTypeMapper ticketTypeMapper;
    private final PromoMapper promoMapper;

    public EventMapper(TicketTypeMapper ticketTypeMapper, PromoMapper promoMapper) {
        this.ticketTypeMapper = ticketTypeMapper;
        this.promoMapper = promoMapper;
    }

    public EventsDTO toDTO(Events event) {
        if (event == null) {
            return null;
        }

        EventsDTO dto = new EventsDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setStart(event.getStartDate());
        dto.setEnd(event.getEndDate());
        dto.setPic(event.getPic());
        dto.setOrganization(event.getOrganization());
        dto.setLocation(event.getLocation());
        dto.setDescription(event.getDescription());
        dto.setCreatedAt(event.getCreatedAt());
        dto.setUpdatedAt(event.getUpdatedAt());
        dto.setDeletedAt(event.getDeletedAt());

        if (event.getEventCategory() != null) {
            dto.setEventCategory(new EventCategoryDTO(event.getEventCategory()));
        }

        if (event.getUser() != null) {
            dto.setUser(new UserDTO(event.getUser()));
        }

        dto.setTicketTypes(event.getTicketTypes() == null ? Collections.emptyList() :
                event.getTicketTypes().stream()
                        .map(ticketTypeMapper::toDTO)
                        .collect(Collectors.toList()));

        dto.setPromos(event.getPromos() == null ? Collections.emptyList() :
                event.getPromos().stream()
                        .map(promoMapper::toDTO)
                        .collect(Collectors.toList()));

        return dto;
    }
}
