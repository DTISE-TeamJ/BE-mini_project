package com.example.BE_mini_project.transaction.controller;

import com.example.BE_mini_project.transaction.dto.AnalyticsDTO;
import com.example.BE_mini_project.transaction.service.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private AnalyticsService analyticsService;

    @GetMapping("/{eventId}")
    public ResponseEntity<List<AnalyticsDTO>> getRevenue(
            @PathVariable Long eventId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam String groupBy) {

        List<AnalyticsDTO> revenue = analyticsService.getRevenue(eventId, start, end, groupBy);
        return ResponseEntity.ok(revenue);
    }

}
