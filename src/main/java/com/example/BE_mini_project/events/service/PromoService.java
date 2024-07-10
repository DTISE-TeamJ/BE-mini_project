package com.example.BE_mini_project.events.service;

import com.example.BE_mini_project.events.dto.PromoDTO;
import com.example.BE_mini_project.events.exception.ResourceNotFoundException;
import com.example.BE_mini_project.events.mapper.PromoMapper;
import com.example.BE_mini_project.events.model.*;
import com.example.BE_mini_project.events.repository.PromoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromoService {
    private final PromoRepository promoRepository;
    private final PromoMapper promoMapper;

    public PromoService(PromoRepository promoRepository, PromoMapper promoMapper) {
        this.promoRepository = promoRepository;
        this.promoMapper = promoMapper;
    }

    @Transactional
    public List<Promo> createPromos(List<PromoDTO> promoDTOs, Events event) {
        List<Promo> promos = promoDTOs.stream()
                .map(promoDTO -> {
                    Promo promo = promoMapper.toEntity(promoDTO);
                    promo.setEvent(event);
                    return promo;
                })
                .collect(Collectors.toList());
        return promoRepository.saveAll(promos);
    }

    public List<PromoDTO> getAllPromos() {
        return promoRepository.findAll().stream()
                .map(promoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PromoDTO getPromoById(Long id) {
        Promo promo = promoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promo not found with id: " + id));
        return promoMapper.toDTO(promo);
    }

    public List<PromoDTO> getPromosByEventId(Long eventId) {
        List<Promo> promos = promoRepository.findByEventId(eventId);
        return promos.stream()
                .map(promoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PromoDTO updatePromo(Long id, PromoDTO updatedPromoDTO) {
        Promo existingPromo = promoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promo not found with id: " + id));

        if (updatedPromoDTO.getName() != null) {
            existingPromo.setName(updatedPromoDTO.getName());
        }
        if (updatedPromoDTO.getPromoType() != null) {
            existingPromo.setPromoType(updatedPromoDTO.getPromoType());
        }
        if (updatedPromoDTO.getDiscount() != null) {
            existingPromo.setDiscount(updatedPromoDTO.getDiscount());
        }
        if (updatedPromoDTO.getQuantity() != null) {
            existingPromo.setQuantity(updatedPromoDTO.getQuantity());
        }
        if (updatedPromoDTO.getStartValid() != null) {
            existingPromo.setStartValid(updatedPromoDTO.getStartValid());
        }
        if (updatedPromoDTO.getEndValid() != null) {
            existingPromo.setEndValid(updatedPromoDTO.getEndValid());
        }

        Promo updatedPromo = promoRepository.save(existingPromo);
        return promoMapper.toDTO(updatedPromo);
    }

    public void deletePromo(Long id) {
        if (!promoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Promo not found with id: " + id);
        }
        promoRepository.deleteById(id);
    }
}