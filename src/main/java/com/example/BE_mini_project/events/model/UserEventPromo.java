//package com.example.BE_mini_project.events.model;
//
//import com.example.BE_mini_project.authentication.model.Users;
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Table(name = "user_event_promos")
//@Data
//public class UserEventPromo {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_type_id_gen")
//    @SequenceGenerator(name = "ticket_type_id_gen", sequenceName = "ticket_type_id_seq", allocationSize = 1)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private Users users;
//
//    @ManyToOne
//    @JoinColumn(name = "event_id")
//    private Events events;
//
//    @ManyToOne
//    @JoinColumn(name = "promo_id")
//    private Promo promo;
//}
