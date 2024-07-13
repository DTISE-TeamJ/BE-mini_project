package com.example.BE_mini_project.transaction.model;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CREDIT_CARD("Credit Card"),
    SHOPEEPAY("ShopeePay"),
    GOPAY("GoPay"),
    BANK_TRANSFER("Bank Transfer"),
    QRIS("QRIS"),
    POINT("Point");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

}