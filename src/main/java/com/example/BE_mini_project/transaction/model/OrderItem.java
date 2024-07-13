package com.example.BE_mini_project.transaction.model;

import com.example.BE_mini_project.events.model.Events;
import com.example.BE_mini_project.events.model.Promo;
import com.example.BE_mini_project.events.model.TicketType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_id_gen")
    @SequenceGenerator(name = "order_item_id_gen", sequenceName = "order_item_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "ticket_type_id", referencedColumnName = "id")
    private TicketType ticketType;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "original_price")
    private Double originalPrice;

    @Column(name = "discounted_price")
    private Double discountedPrice;

    @ManyToOne
    @JoinColumn(name = "promo_id")
    private Promo appliedPromo;

    @OneToMany(mappedBy = "orderItem")
    private List<PromoUsage> promoUsages = new ArrayList<>();

}
