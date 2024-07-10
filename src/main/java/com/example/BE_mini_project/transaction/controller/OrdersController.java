package com.example.BE_mini_project.transaction.controller;

import com.example.BE_mini_project.response.CustomResponse;
import com.example.BE_mini_project.transaction.dto.AddOrderItemRequest;
import com.example.BE_mini_project.transaction.dto.AdjustQuantityRequest;
import com.example.BE_mini_project.transaction.dto.OrderDTO;
import com.example.BE_mini_project.transaction.dto.ProcessPaymentRequest;
import com.example.BE_mini_project.transaction.exception.OrderNotFoundException;
import com.example.BE_mini_project.transaction.mapper.OrderMapperManual;
import com.example.BE_mini_project.transaction.service.OrdersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {
    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/add-item")
    public ResponseEntity<OrderDTO> addOrderItem(@RequestBody AddOrderItemRequest request) {
        OrderDTO orderDTO = ordersService.addOrderItem(request);
        return ResponseEntity.ok(orderDTO);
    }

    @PutMapping("/adjust-quantity")
    public ResponseEntity<OrderDTO> adjustOrderItemQuantity(@RequestBody AdjustQuantityRequest request) {
        OrderDTO orderDTO = ordersService.adjustOrderItemQuantity(request);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping("/{orderId}/process-payment")
    public ResponseEntity<CustomResponse<String>> processPayment(@PathVariable Long orderId,
                                                                 @RequestBody ProcessPaymentRequest request) {
        try {
            ordersService.processPayments(orderId, request.getUserId(), request.getPaymentDetails(), request.getPromoId());

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
    public ResponseEntity<?> getUnpaidOrders(@PathVariable Long userId) {
        try {
            List<OrderDTO> unpaidOrderDTOs = ordersService.getUnpaidOrdersByUserId(userId);
            return ResponseEntity.ok(unpaidOrderDTOs);
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/paid/{userId}")
    public ResponseEntity<?> getPaidOrders(@PathVariable Long userId) {
        try {
            List<OrderDTO> paidOrderDTOs = ordersService.getPaidOrdersByUserId(userId);
            return ResponseEntity.ok(paidOrderDTOs);
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

//    @PutMapping("/apply-promo")
//    public ResponseEntity<OrderDTO> applyPromo(@RequestParam Long orderId,
//                                               @RequestParam Long orderItemId,
//                                               @RequestParam Long promoId) {
//        OrderDTO updatedOrder = orderService.applyPromoToOrderItem(orderId, orderItemId, promoId);
//        return ResponseEntity.ok(updatedOrder);
//    }
}
