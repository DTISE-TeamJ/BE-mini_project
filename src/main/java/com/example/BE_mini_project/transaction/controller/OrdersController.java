package com.example.BE_mini_project.transaction.controller;

import com.example.BE_mini_project.response.CustomResponse;
import com.example.BE_mini_project.transaction.dto.*;
import com.example.BE_mini_project.transaction.exception.OrderNotFoundException;
import com.example.BE_mini_project.transaction.model.PaymentMethod;
import com.example.BE_mini_project.transaction.service.OrdersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {
    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/add-item")
    public ResponseEntity<CustomResponse<OrderDTO>> addOrderItem(@RequestBody AddOrderItemRequest request) {
        OrderDTO orderDTO = ordersService.addOrderItem(request);
        CustomResponse<OrderDTO> response = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "Order item added successfully",
                orderDTO
        );
        return response.toResponseEntity();
    }

    @PutMapping("/adjust-quantity")
    public ResponseEntity<CustomResponse<OrderDTO>> adjustOrderItemQuantity(@RequestBody AdjustQuantityRequest request) {
        OrderDTO orderDTO = ordersService.adjustOrderItemQuantity(request);
        CustomResponse<OrderDTO> response = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "Order item quantity adjusted successfully",
                orderDTO
        );
        return response.toResponseEntity();
    }

    @PostMapping("/process-payment")
    public ResponseEntity<CustomResponse<String>> processPayment(@RequestBody ProcessPaymentRequest request) {
        try {
            ordersService.processPayments(request.getUserId(), request.getOrderId(), request.getPaymentDetails());
            CustomResponse<String> response = new CustomResponse<>(
                    HttpStatus.OK,
                    "Success",
                    "Payment processed successfully",
                    "Transaction completed"
            );
            return response.toResponseEntity();
        } catch (RuntimeException e) {
            CustomResponse<String> errorResponse = new CustomResponse<>(
                    HttpStatus.BAD_REQUEST,
                    "Error",
                    e.getMessage(),
                    null
            );
            return errorResponse.toResponseEntity();
        }
    }

    @GetMapping("/unpaid/{userId}")
    public ResponseEntity<CustomResponse<List<OrderDTO>>> getUnpaidOrders(@PathVariable Long userId) {
        try {
            List<OrderDTO> unpaidOrderDTOs = ordersService.getUnpaidOrdersByUserId(userId);
            CustomResponse<List<OrderDTO>> response = new CustomResponse<>(
                    HttpStatus.OK,
                    "Success",
                    "Unpaid orders retrieved successfully",
                    unpaidOrderDTOs
            );
            return response.toResponseEntity();
        } catch (OrderNotFoundException e) {
            CustomResponse<List<OrderDTO>> errorResponse = new CustomResponse<>(
                    HttpStatus.NOT_FOUND,
                    "Error",
                    e.getMessage(),
                    null
            );
            return errorResponse.toResponseEntity();
        }
    }

    @GetMapping("/paid/{userId}")
    public ResponseEntity<CustomResponse<List<OrderDTO>>> getPaidOrders(@PathVariable Long userId) {
        try {
            List<OrderDTO> paidOrderDTOs = ordersService.getPaidOrdersByUserId(userId);
            CustomResponse<List<OrderDTO>> response = new CustomResponse<>(
                    HttpStatus.OK,
                    "Success",
                    "Paid orders retrieved successfully",
                    paidOrderDTOs
            );
            return response.toResponseEntity();
        } catch (OrderNotFoundException e) {
            CustomResponse<List<OrderDTO>> errorResponse = new CustomResponse<>(
                    HttpStatus.NOT_FOUND,
                    "Error",
                    e.getMessage(),
                    null
            );
            return errorResponse.toResponseEntity();
        }
    }

    @PostMapping("/apply-promo")
    public ResponseEntity<CustomResponse<OrderDTO>> applyPromoCode(@RequestBody ApplyPromoRequest request) {
        OrderDTO updatedOrder = ordersService.applyPromoCode(request.getOrderId(), request.getEventId(), request.getPromoCode(), request.getUserId());
        CustomResponse<OrderDTO> response = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "Promo code applied successfully",
                updatedOrder
        );
        return response.toResponseEntity();
    }

    @PostMapping("/remove-promo")
    public ResponseEntity<CustomResponse<OrderDTO>> removePromo(@RequestBody RemovePromoRequest request) {
        OrderDTO updatedOrder = ordersService.removePromoCode(request.getOrderId(), request.getEventId());
        CustomResponse<OrderDTO> response = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "Promo code removed successfully",
                updatedOrder
        );
        return response.toResponseEntity();
    }

    @GetMapping("/payment-methods")
    public List<Map<String, String>> getPaymentMethods() {
        return Arrays.stream(PaymentMethod.values())
                .map(method -> Map.of(
                        "name", method.name(),
                        "displayName", method.getDisplayName()
                ))
                .collect(Collectors.toList());
    }
}