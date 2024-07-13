package com.example.BE_mini_project.transaction.service;

import com.example.BE_mini_project.events.model.Promo;
import com.example.BE_mini_project.events.repository.PromoRepository;
import com.example.BE_mini_project.transaction.dto.OrderDTO;
import com.example.BE_mini_project.transaction.model.OrderItem;
import com.example.BE_mini_project.transaction.model.Orders;
import com.example.BE_mini_project.transaction.repository.OrdersRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderItemService {

    private final OrdersRepository ordersRepository;
    private final PromoRepository promoRepository;

    public OrderItemService(OrdersRepository ordersRepository, PromoRepository promoRepository) {
        this.ordersRepository = ordersRepository;
        this.promoRepository = promoRepository;
    }


//    @Transactional
//    public OrderDTO applyPromoCode(Long orderId, Long eventId, String promoCode) {
//        Orders order = ordersRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        Promo promo = promoRepository.findByPromoCodeAndEventId(promoCode, eventId)
//                .orElseThrow(() -> new RuntimeException("Invalid promo code for this event"));
//
//        Map<Long, List<OrderItem>> groupedItems = groupOrderItemsByEvent(order);
//        List<OrderItem> eventItems = groupedItems.get(eventId);
//
//        if (eventItems == null || eventItems.isEmpty()) {
//            throw new RuntimeException("No items for this event in the order");
//        }
//
//        double totalBeforeDiscount = eventItems.stream()
//                .mapToDouble(OrderItem::getOriginalPrice)
//                .sum();
//
//        double discountPercentage = promo.getDiscount();
//        double discountMultiplier = 1 - (discountPercentage / 100.0);
//
//        for (OrderItem item : eventItems) {
//            item.setDiscountedPrice(item.getOriginalPrice() * discountMultiplier);
//            item.setAppliedPromo(promo);
//        }
//
//        sumFinalPrice(order);
//        Orders savedOrder = ordersRepository.save(order);
//        return orderMapperManual.toDTO(savedOrder);
//    }
//
//    private double calculateDiscount(double amount, int discountPercentage) {
//        return amount * (discountPercentage / 100.0);
//    }
//
//    private Map<Long, List<OrderItem>> groupOrderItemsByEvent(Orders order) {
//        return order.getOrderItems().stream()
//                .collect(Collectors.groupingBy(item -> item.getTicketType().getEvent().getId()));
//    }

}
