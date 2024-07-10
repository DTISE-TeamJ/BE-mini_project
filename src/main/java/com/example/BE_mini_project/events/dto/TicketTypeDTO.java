package com.example.BE_mini_project.events.dto;

import com.example.BE_mini_project.events.model.Events;
import com.example.BE_mini_project.events.model.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketTypeDTO {

    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
    private Long eventId;

    public TicketTypeDTO(String name, Double price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public TicketTypeDTO(TicketType ticketType) {
        this.id = ticketType.getId();
        this.name = ticketType.getName();
        this.price = ticketType.getPrice();
        this.quantity = ticketType.getQuantity();
        this.eventId = ticketType.getEvent() != null ? ticketType.getEvent().getId() : null;
    }
}
