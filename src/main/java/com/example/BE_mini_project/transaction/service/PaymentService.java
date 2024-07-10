package com.example.BE_mini_project.transaction.service;

import com.example.BE_mini_project.transaction.model.Payment;
import com.example.BE_mini_project.transaction.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }
}
