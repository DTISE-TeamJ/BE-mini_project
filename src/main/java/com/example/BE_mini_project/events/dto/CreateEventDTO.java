package com.example.BE_mini_project.events.dto;

import com.example.BE_mini_project.events.configuration.LocalDateTimeDeserializer;
import com.example.BE_mini_project.events.model.Promo;
import com.example.BE_mini_project.events.model.TicketType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Data
public class  CreateEventDTO {
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    private LocalDateTime date;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    private String name;
    private String start;
    private String end;
    private String pic;
    private String organization;
    private String location;
    private String description;
    private boolean isFree;
    private Long eventCategoryId;
    private Long userId; // Add this line
    private List<TicketTypeDTO> ticketTypes;
    private List<PromoDTO> promos;
}