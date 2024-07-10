package com.example.BE_mini_project.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResult {
    private int pointsUsed;
    private double remainingAmount;
}
