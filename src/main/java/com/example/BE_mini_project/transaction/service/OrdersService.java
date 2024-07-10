package com.example.BE_mini_project.transaction.service;

import com.example.BE_mini_project.authentication.dto.PaymentResult;
import com.example.BE_mini_project.authentication.model.Users;
import com.example.BE_mini_project.authentication.repository.PointRepository;
import com.example.BE_mini_project.authentication.repository.UsersRepository;
import com.example.BE_mini_project.authentication.service.PointService;
import com.example.BE_mini_project.events.model.Events;
import com.example.BE_mini_project.events.model.Promo;
import com.example.BE_mini_project.events.model.TicketType;
import com.example.BE_mini_project.events.repository.PromoRepository;
import com.example.BE_mini_project.events.repository.TicketTypeRepository;
import com.example.BE_mini_project.transaction.dto.AddOrderItemRequest;
import com.example.BE_mini_project.transaction.dto.AdjustQuantityRequest;
import com.example.BE_mini_project.transaction.dto.OrderDTO;
import com.example.BE_mini_project.transaction.dto.OrderItemDTO;
import com.example.BE_mini_project.transaction.exception.OrderNotFoundException;
import com.example.BE_mini_project.transaction.mapper.OrderMapperManual;
import com.example.BE_mini_project.transaction.model.*;
import com.example.BE_mini_project.transaction.repository.OrderItemRepository;
import com.example.BE_mini_project.transaction.repository.OrdersRepository;
import com.example.BE_mini_project.transaction.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final PaymentRepository paymentRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final OrderItemRepository orderItemRepository;
    private final UsersRepository usersRepository;
    private final OrderMapperManual orderMapperManual;
    private final PointService pointService;
    private final PromoRepository promoRepository;

    public OrdersService(OrdersRepository ordersRepository, PaymentRepository paymentRepository, TicketTypeRepository ticketTypeRepository, OrderItemRepository orderItemRepository, UsersRepository usersRepository, OrderMapperManual orderMapperManual, PointRepository pointRepository, PointService pointService, PromoRepository promoRepository) {
        this.ordersRepository = ordersRepository;
        this.paymentRepository = paymentRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.orderItemRepository = orderItemRepository;
        this.usersRepository = usersRepository;
        this.orderMapperManual = orderMapperManual;
        this.pointService = pointService;
        this.promoRepository = promoRepository;
    }

    @Transactional
    public OrderDTO addOrderItem(AddOrderItemRequest request) {
        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Orders order = ordersRepository.findUnpaidOrderByUser(user)
                .orElseGet(() -> createNewOrder(user));

        TicketType ticketType = ticketTypeRepository.findById(request.getTicketTypeId())
                .orElseThrow(() -> new RuntimeException("Ticket id not found"));

        Optional<OrderItem> existingOrderItem = order.getOrderItems().stream()
                .filter(item -> item.getTicketType().getId().equals(ticketType.getId()))
                .findFirst();

        if (existingOrderItem.isPresent()) {
            OrderItem item = existingOrderItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.setTicketTotalPrice(item.getQuantity() * ticketType.getPrice());
        } else {

            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setOrder(order);
            newOrderItem.setTicketType(ticketType);
            newOrderItem.setQuantity(request.getQuantity());
            newOrderItem.setTicketTotalPrice(ticketType.getPrice() * request.getQuantity());

            order.getOrderItems().add(newOrderItem);
        }

        sumFinalPrice(order);
        Orders savedOrder = ordersRepository.save(order);
        return orderMapperManual.toDTO(savedOrder);

    }

    @Transactional
    public OrderDTO adjustOrderItemQuantity(AdjustQuantityRequest request) {
        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Orders order = ordersRepository.findUnpaidOrderByUser(user)
                .orElseThrow(() -> new RuntimeException("No unpaid order found for the user"));

        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(request.getOrderItemId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order item not found in the current order"));

        int newQuantity = orderItem.getQuantity() + request.getQuantityChange();
        if (newQuantity <= 0) {
            order.getOrderItems().remove(orderItem);
            orderItemRepository.delete(orderItem);
        } else {
            orderItem.setQuantity(newQuantity);
            orderItem.setTicketTotalPrice(newQuantity * orderItem.getTicketType().getPrice());
        }

        sumFinalPrice(order);
        Orders savedOrder = ordersRepository.save(order);
        return orderMapperManual.toDTO(savedOrder);
    }

    private Orders createNewOrder(Users user) {
        Orders newOrder = new Orders();
        newOrder.setUser(user);
        newOrder.setPaid(false);
        newOrder.setFinalPrice(0.0);
        return ordersRepository.save(newOrder);
    }

    private void sumFinalPrice(Orders order) {
        double finalPrice = order.getOrderItems().stream()
                .mapToDouble(OrderItem::getTicketTotalPrice)
                .sum();
        order.setFinalPrice(finalPrice);
    }

    @Transactional
    public void processPayments(Long orderId, Long userId, Map<PaymentMethod, Double> paymentDetails, Long promoId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        double totalPaid = 0;
        double remainingAmount = order.getFinalPrice();

        if (paymentDetails.containsKey(PaymentMethod.POINT)) {
            PaymentResult pointResult = pointService.usePointsForPayment(userId,remainingAmount);
            totalPaid += pointResult.getPointsUsed();
            remainingAmount = pointResult.getRemainingAmount();

            Payment pointPayment = new Payment();
            pointPayment.setOrder(order);
            pointPayment.setPaymentMethod(PaymentMethod.POINT);
            pointPayment.setAmount(pointResult.getPointsUsed());
            paymentRepository.save(pointPayment);
            order.getPayments().add(pointPayment);
        }


        for (Map.Entry<PaymentMethod, Double> entry : paymentDetails.entrySet()) {
            if (entry.getKey() == PaymentMethod.POINT) continue;

            double amount = Math.min(entry.getValue(), remainingAmount);
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setPaymentMethod(entry.getKey());
            payment.setAmount(amount);

            paymentRepository.save(payment);
            order.getPayments().add(payment);

            totalPaid += amount;
            remainingAmount -= amount;
        }

        if (Math.abs(totalPaid - order.getFinalPrice()) > 0.01) {
            throw new RuntimeException(
                    "Total payment does not match with the order's final price" +
                    "\nTotal payment to be made: " + order.getFinalPrice() +
                    "\nTotal paid: " + totalPaid +
                    "\nRemaining amount to be paid: " + Math.abs(totalPaid - order.getFinalPrice())
                    );
        }

        order.setPaid(true);

        if (promoId != null) {
            Promo promo = promoRepository.findById(promoId)
                    .orElseThrow(() -> new RuntimeException("Promo not found"));

            // Apply promo discount logic here
            // ...

            // Record promo usage
//            userEventPromoService.recordPromoUsage(user, order.getId(), promo);
        }

        ordersRepository.save(order);
    }

    public List<OrderDTO> getUnpaidOrdersByUserId(Long userId) {
        List<Orders> unpaidOrders = ordersRepository.findByUserIdAndIsPaidFalse(userId);
        if (unpaidOrders.isEmpty()) {
            throw new OrderNotFoundException("No unpaid orders found for this user, user ID: " + userId);
        }
        return unpaidOrders.stream()
                .map(orderMapperManual::toDTO)
                .collect(Collectors.toList());
    }

    private OrderDTO manualConvertToDTOOrders(Orders order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(Long.valueOf(order.getUser().getId()));
        dto.setFinalPrice(order.getFinalPrice());
        dto.setPaid(order.isPaid());

        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                .map(this::convertOrderItemToDTO)
                .collect(Collectors.toList());
        dto.setOrderItems(orderItemDTOs);

        return dto;
    }

    private OrderItemDTO convertOrderItemToDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setTicketTypeId(orderItem.getTicketType().getId());
        dto.setTicketName(orderItem.getTicketType().getName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setTicketTotalPrice(orderItem.getTicketTotalPrice());
//        dto.setEventName(orderItem.getTicketType().getEvent().getName());

        return dto;
    }

    public List<OrderDTO> getPaidOrdersByUserId(Long userId) {
        List<Orders> paidOrders = ordersRepository.findByUserIdAndIsPaidTrue(userId);
        if (paidOrders.isEmpty()) {
            throw new OrderNotFoundException("No unpaid orders found for this user, user ID: " + userId);
        }
        return paidOrders.stream()
                .map(orderMapperManual::toDTO)
                .collect(Collectors.toList());
    }


//    public OrderDTO applyPromoToOrderItem(Long orderId, Long orderItemId, Long promoId) {
//        Orders order = ordersRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        OrderItem orderItem = order.getOrderItems().stream()
//                .filter(item -> item.getId().equals(orderItemId))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Order item not found"));
//
//        Promo promo = promoRepository.findById(promoId)
//                .orElseThrow(() -> new RuntimeException("Promo not found"));
//
//        // Check if promo is valid for this event
//        if (!isPromoValidForEvent(promo, orderItem.getTicketType().getEvent())) {
//            throw new RuntimeException("Promo is not valid for this event");
//        }
//
//        // Apply discount
//        double discountAmount = calculateDiscount(orderItem.getTicketTotalPrice(), promo.getDiscount());
//        orderItem.setAppliedPromoId(promo.getId());
//        orderItem.setTicketTotalPrice(orderItem.getTicketTotalPrice() - discountAmount);
//
//        // Recalculate order total
//        sumFinalPrice(order);
//
//        // Save changes
//        Orders updatedOrder = ordersRepository.save(order);
//
//        // Convert to DTO and return
//        return orderMapper.toDTO(updatedOrder);
//    }
//
//    private boolean isPromoValidForEvent(Promo promo, Event event) {
//        // Implementation depends on how you've structured the relationship between promos and events
//        // This could involve checking the user_event_promos table, or some other logic
//        return true; // Placeholder
//    }
//
//    private double calculateDiscount(double originalPrice, int discountPercentage) {
//        return originalPrice * (discountPercentage / 100.0);
//    }




}
