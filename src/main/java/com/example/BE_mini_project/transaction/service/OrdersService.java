package com.example.BE_mini_project.transaction.service;

import com.example.BE_mini_project.authentication.dto.PaymentResult;
import com.example.BE_mini_project.authentication.model.Discount;
import com.example.BE_mini_project.authentication.model.Users;
import com.example.BE_mini_project.authentication.repository.DiscountRepository;
import com.example.BE_mini_project.authentication.repository.PointRepository;
import com.example.BE_mini_project.authentication.repository.UsersRepository;
import com.example.BE_mini_project.authentication.service.PointService;
import com.example.BE_mini_project.events.model.Promo;
import com.example.BE_mini_project.events.model.PromoType;
import com.example.BE_mini_project.events.model.TicketType;
import com.example.BE_mini_project.events.repository.PromoRepository;
import com.example.BE_mini_project.events.repository.TicketTypeRepository;
import com.example.BE_mini_project.transaction.dto.AddOrderItemRequest;
import com.example.BE_mini_project.transaction.dto.AdjustQuantityRequest;
import com.example.BE_mini_project.transaction.dto.OrderDTO;
import com.example.BE_mini_project.transaction.exception.OrderNotFoundException;
import com.example.BE_mini_project.transaction.mapper.OrderMapperManual;
import com.example.BE_mini_project.transaction.model.*;
import com.example.BE_mini_project.transaction.repository.OrderItemRepository;
import com.example.BE_mini_project.transaction.repository.OrdersRepository;
import com.example.BE_mini_project.transaction.repository.PaymentRepository;
import com.example.BE_mini_project.transaction.repository.PromoUsageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final PromoUsageRepository promoUsageRepository;
    private final DiscountRepository discountRepository;

    public OrdersService(OrdersRepository ordersRepository, PaymentRepository paymentRepository, TicketTypeRepository ticketTypeRepository, OrderItemRepository orderItemRepository, UsersRepository usersRepository, OrderMapperManual orderMapperManual, PointRepository pointRepository, PointService pointService, PromoRepository promoRepository, PromoUsageRepository promoUsageRepository, DiscountRepository discountRepository) {
        this.ordersRepository = ordersRepository;
        this.paymentRepository = paymentRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.orderItemRepository = orderItemRepository;
        this.usersRepository = usersRepository;
        this.orderMapperManual = orderMapperManual;
        this.pointService = pointService;
        this.promoRepository = promoRepository;
        this.promoUsageRepository = promoUsageRepository;
        this.discountRepository = discountRepository;
    }

    @Transactional
    public OrderDTO addOrderItem(AddOrderItemRequest request) {
        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Orders order = ordersRepository.findUnpaidOrderByUser(user)
                .orElseGet(() -> createNewOrder(user));

        for (AddOrderItemRequest.TicketRequest ticketRequest : request.getTicketRequests()) {
            processTicketRequest(order, ticketRequest);
        }

        sumFinalPrice(order);
        Orders savedOrder = ordersRepository.save(order);
        return orderMapperManual.toDTO(savedOrder);
    }

    private void processTicketRequest(Orders order, AddOrderItemRequest.TicketRequest ticketRequest) {
        TicketType ticketType = ticketTypeRepository.findById(ticketRequest.getTicketTypeId())
                .orElseThrow(() -> new RuntimeException("Ticket id not found"));

        int totalOrderedQuantity = ordersRepository.findAll().stream()
                .flatMap(o -> o.getOrderItems().stream())
                .filter(item -> item.getTicketType().getId().equals(ticketType.getId()))
                .mapToInt(OrderItem::getQuantity)
                .sum();

        int remainingQuantity = ticketType.getQuantity() - totalOrderedQuantity;

        Optional<OrderItem> existingOrderItem = order.getOrderItems().stream()
                .filter(item -> item.getTicketType().getId().equals(ticketType.getId()))
                .findFirst();

        int newQuantity;
        if (existingOrderItem.isPresent()) {
            OrderItem item = existingOrderItem.get();
            newQuantity = item.getQuantity() + ticketRequest.getQuantity();
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }
            if (newQuantity > remainingQuantity + item.getQuantity()) {
                throw new IllegalArgumentException("Requested quantity exceeds available tickets");
            }
            item.setQuantity(newQuantity);
            item.setOriginalPrice(newQuantity * ticketType.getPrice());
            item.setDiscountedPrice(item.getOriginalPrice());
        } else {
            newQuantity = ticketRequest.getQuantity();
            if (newQuantity <= 0) {
                throw new IllegalArgumentException("Quantity must be at least 1 for a new order item");
            }
            if (newQuantity > remainingQuantity) {
                throw new IllegalArgumentException("Requested quantity exceeds available tickets");
            }
            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setOrder(order);
            newOrderItem.setTicketType(ticketType);
            newOrderItem.setQuantity(newQuantity);
            newOrderItem.setOriginalPrice(ticketType.getPrice() * newQuantity);
            newOrderItem.setDiscountedPrice(newOrderItem.getOriginalPrice());
            order.getOrderItems().add(newOrderItem);
        }
    }

    public OrderDTO adjustOrderItemQuantity(AdjustQuantityRequest request) {
        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Orders order = ordersRepository.findUnpaidOrderByUser(user)
                .orElseThrow(() -> new RuntimeException("No unpaid order found for the user"));

        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(request.getOrderItemId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order item not found in the current order"));

        TicketType ticketType = orderItem.getTicketType();

        int totalOrderedQuantity = ordersRepository.findAll().stream()
                .flatMap(o -> o.getOrderItems().stream())
                .filter(item -> item.getTicketType().getId().equals(ticketType.getId()) && !item.getId().equals(orderItem.getId()))
                .mapToInt(OrderItem::getQuantity)
                .sum();

        int remainingQuantity = ticketType.getQuantity() - totalOrderedQuantity;

        int newQuantity = orderItem.getQuantity() + request.getQuantityChange();

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        } else if (newQuantity == 0) {
            order.getOrderItems().remove(orderItem);
            orderItemRepository.delete(orderItem);
        } else if (newQuantity > remainingQuantity) {
            throw new IllegalArgumentException("Requested quantity exceeds available tickets");
        } else {
            orderItem.setQuantity(newQuantity);
            orderItem.setOriginalPrice(newQuantity * ticketType.getPrice());
        }

        sumFinalPrice(order);
        Orders savedOrder = ordersRepository.save(order);
        return orderMapperManual.toDTO(savedOrder);
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

    public List<OrderDTO> getPaidOrdersByUserId(Long userId) {
        List<Orders> paidOrders = ordersRepository.findByUserIdAndIsPaidTrue(userId);
        if (paidOrders.isEmpty()) {
            throw new OrderNotFoundException("No unpaid orders found for this user, user ID: " + userId);
        }
        return paidOrders.stream()
                .map(orderMapperManual::toDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO applyPromoCode(Long orderId, Long eventId, String promoCode, Long userId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.isPaid()) {
            throw new RuntimeException("Cannot apply promo code. The order has already been paid");
        }

        Promo promo = promoRepository.findByPromoCodeAndEventId(promoCode, eventId)
                .orElseThrow(() -> new RuntimeException("Invalid promo code for this event"));

        if (promo.getPromoType() == PromoType.REFERRAL) {
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Discount discount = user.getDiscount();
            if (discount == null || !discount.isHasDiscount()) {
                throw new RuntimeException("User cannot use the referral promo type");
            }
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(promo.getStartValid()) || now.isAfter(promo.getEndValid())) {
            throw new RuntimeException("Promo code is not valid at this time");
        }

        long usedCount = promoUsageRepository.countByPromoId(promo.getId());
        if (usedCount >= promo.getQuantity()) {
            throw new RuntimeException("Promo code has been fully redeemed");
        }

        Map<Long, List<OrderItem>> groupedItems = groupOrderItemsByEvent(order);
        List<OrderItem> eventItems = groupedItems.get(eventId);

        if (eventItems == null || eventItems.isEmpty()) {
            throw new RuntimeException("No items for this event in the order");
        }

        double discountPercentage = promo.getDiscount();
        double discountMultiplier = 1 - (discountPercentage / 100.0);

        for (OrderItem item : eventItems) {
            double discountedPrice = item.getOriginalPrice() * discountMultiplier;
            item.setDiscountedPrice(discountedPrice);
            item.setAppliedPromo(promo);
        }

        sumFinalPrice(order);
        return orderMapperManual.toDTO(order);
    }

    public OrderDTO removePromoCode(Long orderId, Long eventId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.isPaid()) {
            throw new RuntimeException("Cannot remove the promo code. The order has already been paid");
        }

        Map<Long, List<OrderItem>> groupedItems = groupOrderItemsByEvent(order);
        List<OrderItem> eventItems = groupedItems.get(eventId);

        if (eventItems == null || eventItems.isEmpty()) {
            throw new RuntimeException("No items for this event in the order");
        }

        for (OrderItem item : eventItems) {
            item.setDiscountedPrice(item.getOriginalPrice());
            item.setAppliedPromo(null);
        }

        sumFinalPrice(order);
        Orders savedOrder = ordersRepository.save(order);
        return orderMapperManual.toDTO(savedOrder);
    }

    @Transactional
    public void processPayments(Long orderId, Long userId, Map<PaymentMethod, Double> paymentDetails) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.isPaid()) {
            throw new RuntimeException("Your order has already been paid");
        }

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean referralUsed = false;

        for (OrderItem item : order.getOrderItems()) {
            if (item.getAppliedPromo() != null) {
                Promo promo = item.getAppliedPromo();

                LocalDateTime now = LocalDateTime.now();
                if (now.isBefore(promo.getStartValid()) || now.isAfter(promo.getEndValid())) {
                    throw new RuntimeException("Promo code " + promo.getPromoCode() + " is not valid at this time");
                }

                long usedCount = promoUsageRepository.countByPromoId(promo.getId());
                if (usedCount >= promo.getQuantity()) {
                    throw new RuntimeException("Promo code " + promo.getPromoCode() + " has been fully redeemed");
                }

                PromoUsage promoUsage = new PromoUsage();
                promoUsage.setUser(user);
                promoUsage.setPromo(promo);
                promoUsage.setOrderItem(item);
                promoUsage.setUsedAt(LocalDateTime.now());
                promoUsageRepository.save(promoUsage);

                if (promo.getPromoType() == PromoType.REFERRAL) {
                    referralUsed = true;
                }
            }
        }

        double totalPaid = 0;
        double remainingAmount = order.getFinalPrice();

        if (paymentDetails.containsKey(PaymentMethod.POINT)) {
            PaymentResult pointResult = pointService.usePointsForPayment(userId, remainingAmount);

            if (pointResult.getPointsUsed() > 0) {
                double pointsUsedAsAmount = remainingAmount - pointResult.getRemainingAmount();
                totalPaid += pointsUsedAsAmount;
                remainingAmount = pointResult.getRemainingAmount();

                Payment pointPayment = new Payment();
                pointPayment.setOrder(order);
                pointPayment.setPaymentMethod(PaymentMethod.POINT);
                pointPayment.setAmount(pointsUsedAsAmount);
                paymentRepository.save(pointPayment);
                order.getPayments().add(pointPayment);
            }
        }

        for (Map.Entry<PaymentMethod, Double> entry : paymentDetails.entrySet()) {
            if (entry.getKey() == PaymentMethod.POINT) continue;

            double amount = Math.min(entry.getValue(), remainingAmount);
            if (amount > 0) {
                Payment payment = new Payment();
                payment.setOrder(order);
                payment.setPaymentMethod(entry.getKey());
                payment.setAmount(amount);

                paymentRepository.save(payment);
                order.getPayments().add(payment);

                totalPaid += amount;
                remainingAmount -= amount;
            }
        }

        if (Math.abs(totalPaid - order.getFinalPrice()) > 0.01) {
            throw new RuntimeException(
                    "Total payment does not match with the order's final price" +
                            "\nTotal payment to be made: " + order.getFinalPrice() +
                            "\nTotal paid: " + totalPaid +
                            "\nRemaining amount to be paid: " + remainingAmount
            );
        }

        if (referralUsed) {
            Discount discount = user.getDiscount();
            if (discount != null) {
                discount.setHasDiscount(false);
                discountRepository.save(discount);
            }
        }

        order.setPaidAt(LocalDateTime.now());
        order.setPaid(true);
        ordersRepository.save(order);
    }

    private Orders createNewOrder(Users user) {
        Orders newOrder = new Orders();
        newOrder.setUser(user);
        newOrder.setPaid(false);
        newOrder.setFinalPrice(0.0);
        return ordersRepository.save(newOrder);
    }

    private void sumFinalPrice(Orders order) {
        double totalOriginalPrice = order.getOrderItems().stream()
                .mapToDouble(OrderItem::getOriginalPrice)
                .sum();

        double totalDiscountedPrice = order.getOrderItems().stream()
                .mapToDouble(item -> item.getAppliedPromo() != null ? item.getDiscountedPrice() : item.getOriginalPrice())
                .sum();

        order.setFinalPrice(totalDiscountedPrice);
    }

    private Map<Long, List<OrderItem>> groupOrderItemsByEvent(Orders order) {
        return order.getOrderItems().stream()
                .collect(Collectors.groupingBy(item -> item.getTicketType().getEvent().getId()));
    }




}
