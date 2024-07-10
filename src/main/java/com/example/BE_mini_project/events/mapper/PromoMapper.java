package com.example.BE_mini_project.events.mapper;

import com.example.BE_mini_project.events.dto.PromoDTO;
import com.example.BE_mini_project.events.dto.TicketTypeDTO;
import com.example.BE_mini_project.events.model.Promo;
import com.example.BE_mini_project.events.model.TicketType;
import org.springframework.stereotype.Component;

@Component
public class PromoMapper {
    public PromoDTO toDTO(Promo promo) {
        if (promo == null) {
            return null;
        }
        PromoDTO dto = new PromoDTO();
        dto.setName(promo.getName());
        dto.setPromoType(promo.getPromoType());
        dto.setDiscount(promo.getDiscount());
        dto.setQuantity(promo.getQuantity());
        dto.setStartValid(promo.getStartValid());
        dto.setEndValid(promo.getEndValid());
        return dto;
    }

    public Promo toEntity(PromoDTO dto) {
        if (dto == null) {
            return null;
        }
        Promo promo = new Promo();
        promo.setName(dto.getName());
        promo.setPromoType(dto.getPromoType());
        promo.setDiscount(dto.getDiscount());
        promo.setQuantity(dto.getQuantity());
        promo.setStartValid(dto.getStartValid());
        promo.setEndValid(dto.getEndValid());
        return promo;
    }
}
