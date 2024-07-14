package com.example.BE_mini_project.transaction.controller;

import com.example.BE_mini_project.response.CustomResponse;
import com.example.BE_mini_project.transaction.service.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<CustomResponse<Map<String, Double>>> getRevenueForCreator(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "monthly") String interval) {

        Map<String, Double> revenue = analyticsService.calculateRevenueForCreator(userId, startDate, endDate, interval);

        CustomResponse<Map<String, Double>> response = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "Revenue data retrieved successfully",
                revenue
        );

        return response.toResponseEntity();
    }

}
