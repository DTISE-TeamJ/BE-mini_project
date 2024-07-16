package com.example.BE_mini_project.events.controller;

import com.example.BE_mini_project.events.dto.CreateReviewDTO;
import com.example.BE_mini_project.events.model.Review;
import com.example.BE_mini_project.events.service.ReviewService;
import com.example.BE_mini_project.response.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<CustomResponse<Review>> createReview(@RequestBody CreateReviewDTO createReviewDTO, @RequestParam Long userId) {
        try {
            Review createdReview = reviewService.createReview(createReviewDTO, userId);
            CustomResponse<Review> response = new CustomResponse<>(
                    HttpStatus.CREATED,
                    "Created",
                    "Review created successfully",
                    createdReview
            );
            return response.toResponseEntity();
        } catch (RuntimeException e) {
            CustomResponse<Review> response = new CustomResponse<>(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
            return response.toResponseEntity();
        }
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<CustomResponse<List<Review>>> getReviewsByEventId(@PathVariable Long eventId) {
        List<Review> reviews = reviewService.getReviewsByEventId(eventId);
        CustomResponse<List<Review>> response = new CustomResponse<>(
                HttpStatus.OK,
                "OK",
                "Reviews retrieved successfully",
                reviews
        );
        return response.toResponseEntity();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CustomResponse<List<Review>>> getReviewsByUserId(@PathVariable Long userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        CustomResponse<List<Review>> response = new CustomResponse<>(
                HttpStatus.OK,
                "OK",
                "Reviews retrieved successfully",
                reviews
        );
        return response.toResponseEntity();
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<CustomResponse<Review>> getReviewById(@PathVariable Long reviewId) {
        try {
            Review review = reviewService.getReviewById(reviewId);
            CustomResponse<Review> response = new CustomResponse<>(
                    HttpStatus.OK,
                    "OK",
                    "Review retrieved successfully",
                    review
            );
            return response.toResponseEntity();
        } catch (RuntimeException e) {
            CustomResponse<Review> response = new CustomResponse<>(
                    HttpStatus.NOT_FOUND,
                    e.getMessage()
            );
            return response.toResponseEntity();
        }
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<CustomResponse<Void>> deleteReview(@PathVariable Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            CustomResponse<Void> response = new CustomResponse<>(
                    HttpStatus.OK,
                    "OK",
                    "Review deleted successfully",
                    null
            );
            return response.toResponseEntity();
        } catch (RuntimeException e) {
            CustomResponse<Void> response = new CustomResponse<>(
                    HttpStatus.NOT_FOUND,
                    e.getMessage()
            );
            return response.toResponseEntity();
        }
    }
}