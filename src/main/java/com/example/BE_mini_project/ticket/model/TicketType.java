package com.example.BE_mini_project.ticket.model;

import com.example.BE_mini_project.events.model.Events;
import jakarta.persistence.*;

@Entity
@Table(name = "ticket_type")
public class TicketType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private Integer quantity;
//    private Integer sold = 0; // Add this field to track sold tickets


    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Events event;  // Changed from 'events' to 'event'

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Events getEvent() {
        return event;
    }

    public void setEvent(Events event) {
        this.event = event;
    }

//    public Integer getSold() {
//        return sold;
//    }
//
//    public void setSold(Integer sold) {
//        this.sold = sold;
//    }
}