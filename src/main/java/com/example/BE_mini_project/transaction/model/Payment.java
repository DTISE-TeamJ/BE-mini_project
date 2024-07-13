package com.example.BE_mini_project.transaction.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "payment")
public class
Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_id_gen")
    @SequenceGenerator(name = "payment_id_gen", sequenceName = "payment_id_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Orders order;

    @Column(name = "amount")
    private double amount;

}
