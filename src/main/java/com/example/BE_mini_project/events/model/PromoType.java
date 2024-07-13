package com.example.BE_mini_project.events.model;


import lombok.Getter;

@Getter
public enum PromoType {
    REFERRAL("Referral"),
    EVENT_CREATOR_DISCOUNT("Event Creator Discount");

    private final String displayName;

    PromoType(String displayName) {
        this.displayName = displayName;
    }
}