package com.example.BE_mini_project.events.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateEventDTO {
    private String name;
    private LocalDateTime date;
    private String start;
    private String end;
    private String pic;
    private String organization;
    private String location;
    private String description;
    private boolean isFree;
    private Long eventCategoryId;
}
