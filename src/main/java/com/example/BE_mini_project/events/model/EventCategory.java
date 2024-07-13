package com.example.BE_mini_project.events.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "event_category")
public class EventCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "eventCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Events> events;

}