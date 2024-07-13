package com.example.BE_mini_project.transaction.dto;

import lombok.Data;

@Data
public class ApplyPromoRequest {
    private Long orderId;
    private Long eventId;
    private String promoCode;
    private Long userId;
}
