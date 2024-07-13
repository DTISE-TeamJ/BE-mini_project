package com.example.BE_mini_project.events.controller;

import com.example.BE_mini_project.events.dto.PromoDTO;
import com.example.BE_mini_project.events.model.PromoType;
import com.example.BE_mini_project.events.service.PromoService;
import com.example.BE_mini_project.response.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/promos")
public class PromoController {

    private final PromoService promoService;

    public PromoController(PromoService promoService) {
        this.promoService = promoService;
    }

    @GetMapping
    public ResponseEntity<CustomResponse<List<PromoDTO>>> getAllPromos() {
        List<PromoDTO> promos = promoService.getAllPromos();

        CustomResponse<List<PromoDTO>> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "All promos successfully retrieved",
                promos
        );

        return customResponse.toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<PromoDTO>> getPromoById(@PathVariable Long id) {
        PromoDTO promo = promoService.getPromoById(id);

        CustomResponse<PromoDTO> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "Promo by ID successfully retrieved",
                promo
        );

        return customResponse.toResponseEntity();
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<CustomResponse<List<PromoDTO>>> getPromosByEventId(@PathVariable Long eventId) {
        List<PromoDTO> promos = promoService.getPromosByEventId(eventId);

        CustomResponse<List<PromoDTO>> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "Promos by event successfully retrieved",
                promos
        );

        return customResponse.toResponseEntity();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomResponse<PromoDTO>> updatePromo(@PathVariable Long id, @RequestBody PromoDTO promoDTO) {
        PromoDTO updatedPromo = promoService.updatePromo(id, promoDTO);

        CustomResponse<PromoDTO> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Updated",
                "Promo successfully updated",
                updatedPromo
        );

        return customResponse.toResponseEntity();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomResponse<Void>> deletePromo(@PathVariable Long id) {
        promoService.deletePromo(id);

        CustomResponse<Void> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Deleted",
                "Promo successfully deleted",
                null
        );

        return customResponse.toResponseEntity();
    }

    @GetMapping("/types")
    public ResponseEntity<CustomResponse<List<Map<String, String>>>> getPromoTypes() {
        List<Map<String, String>> promoTypes = Arrays.stream(PromoType.values())
                .map(type -> Map.of(
                        "code", type.name(),
                        "displayName", type.getDisplayName()
                ))
                .collect(Collectors.toList());

        CustomResponse<List<Map<String, String>>> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "All promo types successfully retrieved",
                promoTypes
        );

        return customResponse.toResponseEntity();
    }

}
