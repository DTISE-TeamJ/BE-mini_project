package com.example.BE_mini_project.transaction.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private Long ticketTypeId;
    private String ticketName;
    private int quantity;
    private Double ticketTotalPrice;
    private String eventName;
}
