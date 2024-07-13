package com.example.BE_mini_project.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsDTO {
    private LocalDateTime dateTime;
    private Double revenue;
}
