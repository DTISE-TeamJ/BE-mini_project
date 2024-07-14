package com.example.BE_mini_project.transaction.service;

import com.example.BE_mini_project.transaction.model.OrderItem;
import com.example.BE_mini_project.transaction.model.Orders;
import com.example.BE_mini_project.transaction.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AnalyticsService {
    private final OrdersRepository orderRepository;

    public AnalyticsService(OrdersRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public Map<String, Double> calculateRevenueForCreator(Long userId, LocalDateTime startDate, LocalDateTime endDate, String interval) {
        List<Orders> paidOrders = orderRepository.findPaidOrdersForCreatorBetweenDates(userId, startDate, endDate);

        Map<String, Double> revenue = new TreeMap<>();

        for (Orders order : paidOrders) {
            for (OrderItem item : order.getOrderItems()) {
                Double itemRevenue = item.getDiscountedPrice() * item.getQuantity();
                updateRevenue(revenue, interval, order.getPaidAt(), itemRevenue);
            }
        }

        return revenue;
    }

    private void updateRevenue(Map<String, Double> revenueMap, String interval, LocalDateTime paidAt, Double revenue) {
        String key = getTimeKey(interval, paidAt);
        revenueMap.merge(key, revenue, Double::sum);
    }

    private String getTimeKey(String interval, LocalDateTime dateTime) {
        switch (interval.toLowerCase()) {
            case "monthly":
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            case "daily":
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            case "hourly":
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
            default:
                throw new IllegalArgumentException("Invalid interval: " + interval);
        }
    }
}