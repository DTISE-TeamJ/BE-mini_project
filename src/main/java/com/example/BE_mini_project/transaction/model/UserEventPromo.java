//package com.example.BE_mini_project.transaction.model;
//
//import com.example.BE_mini_project.authentication.model.Users;
//import com.example.BE_mini_project.events.model.Events;
//import com.example.BE_mini_project.events.model.Promo;
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Data
//@Table(name = "user_event_promos")
//public class UserEventPromo {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private Users user;
//
//    @ManyToOne
//    @JoinColumn(name = "event_id", nullable = false)
//    private Events event;
//
//    @ManyToOne
//    @JoinColumn(name = "promo_id", nullable = false)
//    private Promo promo;
//
//}