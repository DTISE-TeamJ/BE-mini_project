package com.example.BE_mini_project.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;
    private Long ticketTypeId;
    private String ticketName;
    private int quantity;
    private Double originalPrice;
    private Double discountedPrice;
    private String appliedPromoCode;
    private String eventName;
}
