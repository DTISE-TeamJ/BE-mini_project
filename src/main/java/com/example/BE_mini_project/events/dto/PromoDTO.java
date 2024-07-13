package com.example.BE_mini_project.events.dto;

import com.example.BE_mini_project.events.configuration.LocalDateTimeDeserializer;
import com.example.BE_mini_project.events.model.Promo;
import com.example.BE_mini_project.events.model.PromoType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PromoDTO {
    private String name;
    private PromoType promoType;
    private String promoTypeDisplayName;
    private Integer discount;
    private Integer quantity;
    private String promoCode;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startValid;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endValid;

    public PromoDTO(Promo promo) {
        this.name = promo.getName();
        this.promoType = promo.getPromoType();
        this.promoTypeDisplayName = promo.getPromoType() != null ? promo.getPromoType().getDisplayName() : null;
        this.discount = promo.getDiscount();
        this.quantity = promo.getQuantity();
        this.promoCode = promo.getPromoCode();
        this.startValid = promo.getStartValid();
        this.endValid = promo.getEndValid();
    }

    public String getPromoTypeDisplayName() {
        return promoType != null ? promoType.getDisplayName() : null;
    }
}