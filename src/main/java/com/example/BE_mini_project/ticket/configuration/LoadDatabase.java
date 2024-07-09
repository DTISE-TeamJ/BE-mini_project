//package com.example.BE_mini_project.ticket.configuration;
//
//import com.example.BE_mini_project.events.model.Events;
//import com.example.BE_mini_project.events.repository.EventRepository;
//import com.example.BE_mini_project.ticket.model.TicketType;
//import com.example.BE_mini_project.ticket.repository.TicketRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class LoadDatabase {
//    @Bean
//    CommandLineRunner initDatabase(EventRepository eventRepository, TicketRepository ticketTypeRepository) {
//        return args -> {
//            // Find all events
//            Iterable<Events> events = eventRepository.findAll();
//            for (Events event : events) {
//                // Check if the event is not free
//                if (!event.getIsFree()) {
//                    // Create and save ticket types
//                    TicketType golden = new TicketType();
//                    golden.setName("golden");
//                    golden.setPrice(50.00);  // Example price
//                    golden.setQuantity(50);
//                    golden.setEvent(event);
//
//                    TicketType platinum = new TicketType();
//                    platinum.setName("platinum");
//                    platinum.setPrice(100.00);  // Example price
//                    platinum.setQuantity(30);
//                    platinum.setEvent(event);
//
//                    TicketType diamond = new TicketType();
//                    diamond.setName("diamond");
//                    diamond.setPrice(200.00);  // Example price
//                    diamond.setQuantity(20);
//                    diamond.setEvent(event);
//
//                    ticketTypeRepository.save(golden);
//                    ticketTypeRepository.save(platinum);
//                    ticketTypeRepository.save(diamond);
//
//                    System.out.println("Ticket types added to the non-free event with ID: " + event.getId());
//                } else {
//                    System.out.println("The event with ID " + event.getId() + " is free. No ticket types added.");
//                }
//            }
//        };
//    }
//}