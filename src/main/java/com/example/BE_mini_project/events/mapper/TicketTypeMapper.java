package com.example.BE_mini_project.events.mapper;

import com.example.BE_mini_project.events.dto.TicketTypeDTO;
import com.example.BE_mini_project.events.model.TicketType;
import org.springframework.stereotype.Component;

@Component
public class TicketTypeMapper {
    public TicketTypeDTO toDTO(TicketType ticketType) {
        if (ticketType == null) {
            return null;
        }
        TicketTypeDTO dto = new TicketTypeDTO();
        dto.setId(ticketType.getId());
        dto.setName(ticketType.getName());
        dto.setPrice(ticketType.getPrice());
        dto.setQuantity(ticketType.getQuantity());
        dto.setEventId(ticketType.getEvent() != null ? ticketType.getEvent().getId() : null);
        return dto;
    }

    public TicketType toEntity(TicketTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        TicketType ticketType = new TicketType();
        ticketType.setId(dto.getId());
        ticketType.setName(dto.getName());
        ticketType.setPrice(dto.getPrice());
        ticketType.setQuantity(dto.getQuantity());
        return ticketType;
    }
}