package com.example.BE_mini_project.transaction.mapper;

import com.example.BE_mini_project.transaction.dto.OrderDTO;
import com.example.BE_mini_project.transaction.dto.OrderItemDTO;
import com.example.BE_mini_project.transaction.model.OrderItem;
import com.example.BE_mini_project.transaction.model.Orders;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapperManual {

    public OrderDTO toDTO(Orders order) {
        if (order == null) {
            return null;
        }

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(Long.valueOf(order.getUser().getId()));
//        dto.setFinalPrice(order.getFinalPrice());
        dto.setPaid(order.isPaid());
        dto.setTotalOriginalPrice(order.getOrderItems().stream()
                .mapToDouble(OrderItem::getOriginalPrice)
                .sum());
        dto.setFinalPrice(order.getOrderItems().stream()
                .mapToDouble(item -> item.getAppliedPromo() != null ? item.getDiscountedPrice() : item.getOriginalPrice())
                .sum());

        List<OrderItemDTO> orderItemDTOs = getOrderItemDTOS(order);
        dto.setOrderItems(orderItemDTOs);

        return dto;
    }

    private static List<OrderItemDTO> getOrderItemDTOS(Orders order) {
        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setTicketTypeId(item.getTicketType().getId());
            itemDTO.setTicketName(item.getTicketType().getName());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setOriginalPrice(item.getOriginalPrice());
            itemDTO.setDiscountedPrice(item.getDiscountedPrice());
            itemDTO.setAppliedPromoCode(item.getAppliedPromo() != null ? item.getAppliedPromo().getPromoCode() : null);
            itemDTO.setEventName(item.getTicketType().getEvent().getName());
            orderItemDTOs.add(itemDTO);
        }
        return orderItemDTOs;
    }
}
