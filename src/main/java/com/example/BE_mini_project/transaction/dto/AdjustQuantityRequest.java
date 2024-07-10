package com.example.BE_mini_project.transaction.dto;

import lombok.Data;

@Data
public class AdjustQuantityRequest {
    private Long userId;
    private Long orderItemId;
    private int quantityChange;
}
