package com.example.BE_mini_project.events.mapper;

import com.example.BE_mini_project.authentication.model.Users;
import com.example.BE_mini_project.events.dto.CreateReviewDTO;
import com.example.BE_mini_project.events.model.Events;
import com.example.BE_mini_project.events.model.Review;

public class ReviewMapper {

    public static Review createReviewDTOToReview(CreateReviewDTO dto, Users user, Events event) {
        Review review = new Review();
        review.setUser(user);
        review.setEvent(event);
        review.setFeedbackGeneral(dto.getFeedbackGeneral());
        review.setFeedbackImprovement(dto.getFeedbackImprovement());
        review.setRating(dto.getRating());
        return review;
    }
}
