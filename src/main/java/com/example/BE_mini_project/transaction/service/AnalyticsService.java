package com.example.BE_mini_project.transaction.service;

import com.example.BE_mini_project.transaction.dto.AnalyticsDTO;
import com.example.BE_mini_project.transaction.repository.OrderItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    private final OrderItemRepository orderItemRepository;
    @PersistenceContext
    private final EntityManager entityManager;

    public AnalyticsService(OrderItemRepository orderItemRepository, EntityManager entityManager) {
        this.orderItemRepository = orderItemRepository;
        this.entityManager = entityManager;
    }

    public List<AnalyticsDTO> getRevenue(Long eventId, LocalDateTime start, LocalDateTime end, String groupBy) {
        String truncateFunction;

        switch (groupBy.toLowerCase()) {
            case "month":
                truncateFunction = "DATE_TRUNC('month', o.created_at)";
                break;
            case "day":
                truncateFunction = "DATE_TRUNC('day', o.created_at)";
                break;
            case "hour":
                truncateFunction = "DATE_TRUNC('hour', o.created_at)";
                break;
            default:
                throw new IllegalArgumentException("Invalid groupBy parameter");
        }

        String query = "SELECT " + truncateFunction + " as date_time, SUM(oi.discounted_price) as revenue " +
                "FROM orders o " +
                "JOIN order_items oi ON o.id = oi.order_id " +
                "WHERE oi.event_id = :eventId " +
                "AND o.created_at BETWEEN :start AND :end " +
                "GROUP BY " + truncateFunction + " " +
                "ORDER BY " + truncateFunction;

        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(query)
                .setParameter("eventId", eventId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();

        return results.stream()
                .map(result -> new AnalyticsDTO(
                        ((Timestamp) result[0]).toLocalDateTime(),
                        ((Number) result[1]).doubleValue()
                ))
                .collect(Collectors.toList());
    }
}