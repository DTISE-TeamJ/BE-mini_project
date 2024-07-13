package com.example.BE_mini_project.transaction.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private List<OrderItemDTO> orderItems;
    private Double finalPrice;
    private Double totalOriginalPrice;
//    private Double totalDiscountedPrice;
    private boolean paid;
}
