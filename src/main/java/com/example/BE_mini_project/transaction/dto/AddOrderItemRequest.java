package com.example.BE_mini_project.transaction.dto;

import lombok.Data;

@Data
public class AddOrderItemRequest {
    private Long userId;
    private Long ticketTypeId;
    private Integer quantity;

}
