package com.example.BE_mini_project.events.dto;

import lombok.Data;

@Data
public class CreateReviewDTO {
    private Long eventId;
    private String feedbackGeneral;
    private String feedbackImprovement;
    private Integer rating;
}