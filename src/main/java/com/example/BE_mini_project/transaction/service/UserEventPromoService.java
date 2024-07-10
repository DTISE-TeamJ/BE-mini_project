//package com.example.BE_mini_project.transaction.service;
//
//import com.example.BE_mini_project.authentication.model.Users;
//import com.example.BE_mini_project.events.model.Events;
//import com.example.BE_mini_project.events.model.Promo;
//import com.example.BE_mini_project.events.model.UserEventPromo;
//import com.example.BE_mini_project.transaction.repository.UserEventPromoRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class UserEventPromoService {
//    private final UserEventPromoRepository userEventPromoRepository;
//
//    public UserEventPromoService(UserEventPromoRepository userEventPromoRepository) {
//        this.userEventPromoRepository = userEventPromoRepository;
//    }
//
//    @Transactional
//    public void recordPromoUsage(Users user, Events event, Promo promo) {
//        UserEventPromo userEventPromo = new UserEventPromo();
//        userEventPromo.setUsers(user);
//        userEventPromo.setEvents(event);
//        userEventPromo.setPromo(promo);
//        userEventPromoRepository.save(userEventPromo);
//    }
//}