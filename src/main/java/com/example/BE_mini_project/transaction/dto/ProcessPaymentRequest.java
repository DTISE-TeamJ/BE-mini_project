package com.example.BE_mini_project.transaction.dto;

import com.example.BE_mini_project.transaction.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ProcessPaymentRequest {
    private Long userId;
    private Map<PaymentMethod, Double> paymentDetails;
    private Long promoId;
}
