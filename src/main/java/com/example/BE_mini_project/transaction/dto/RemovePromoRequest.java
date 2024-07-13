package com.example.BE_mini_project.transaction.dto;

import lombok.Data;

@Data
public class RemovePromoRequest {
    private Long orderId;
    private Long eventId;
}
