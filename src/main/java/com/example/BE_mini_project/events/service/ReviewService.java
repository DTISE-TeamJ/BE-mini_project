package com.example.BE_mini_project.events.service;

import com.example.BE_mini_project.authentication.model.Users;
import com.example.BE_mini_project.authentication.repository.UsersRepository;
import com.example.BE_mini_project.events.dto.CreateReviewDTO;
import com.example.BE_mini_project.events.mapper.ReviewMapper;
import com.example.BE_mini_project.events.model.Events;
import com.example.BE_mini_project.events.model.Review;
import com.example.BE_mini_project.events.repository.EventRepository;
import com.example.BE_mini_project.events.repository.ReviewRepository;
import com.example.BE_mini_project.transaction.repository.OrdersRepository;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UsersRepository usersRepository;
    private final EventRepository eventRepository;
    private final OrdersRepository ordersRepository;

    public ReviewService(ReviewRepository reviewRepository, UsersRepository usersRepository, EventRepository eventRepository, OrdersRepository ordersRepository) {
        this.reviewRepository = reviewRepository;
        this.usersRepository = usersRepository;
        this.eventRepository = eventRepository;
        this.ordersRepository = ordersRepository;
    }

    public Review createReview(CreateReviewDTO createReviewDTO, Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Events event = eventRepository.findById(createReviewDTO.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (LocalDateTime.now().isBefore(event.getEndDate())) {
            throw new RuntimeException("Cannot create review before event has finished");
        }

        boolean attended = hasAttend(Long.valueOf(user.getId()), event.getId());
        if (!attended) {
            throw new RuntimeException("User did not attend this event");
        }

        boolean alreadyReviewed = checkExistingReview(Long.valueOf(user.getId()), event.getId());
        if (alreadyReviewed) {
            throw new RuntimeException("User has already reviewed this event");
        }

        Review review = ReviewMapper.createReviewDTOToReview(createReviewDTO, user, event);

        return reviewRepository.save(review);
    }

    private boolean hasAttend(Long userId, Long eventId) {
        // This implementation assumes you have a Ticket entity that links users to events
        return ordersRepository.existsByUserIdAndEventId(userId, eventId);
    }

    private boolean checkExistingReview(Long userId, Long eventId) {
        return reviewRepository.existsByUserIdAndEventId(userId, eventId);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public List<Review> getReviewsByEventId(Long eventId) {
        return reviewRepository.findByEventId(eventId);
    }

    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public Review updateReview(Review review) {
        // TODO: Add logic to ensure only the user who created the review can update it
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        // TODO: Add logic to ensure only the user who created the review can delete it
        reviewRepository.deleteById(id);
    }

    public double getAverageRatingForEvent(Long eventId) {
        List<Review> reviews = getReviewsByEventId(eventId);
        if (reviews.isEmpty()) {
            return 0;
        }
        double sum = reviews.stream().mapToInt(Review::getRating).sum();
        return sum / reviews.size();
    }
}
