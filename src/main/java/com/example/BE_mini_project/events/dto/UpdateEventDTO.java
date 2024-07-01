package com.example.BE_mini_project.events.dto;

import com.example.BE_mini_project.events.configuration.LocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UpdateEventDTO {
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;

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
    private Long userId;
}
