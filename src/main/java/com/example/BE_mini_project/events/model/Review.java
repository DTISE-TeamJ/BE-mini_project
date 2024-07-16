package com.example.BE_mini_project.events.model;

import com.example.BE_mini_project.authentication.model.Users;
import jakarta.persistence.*;
        import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_id_gen")
    @SequenceGenerator(name = "review_id_gen", sequenceName = "review_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "feedback_general")
    private String feedbackGeneral;

    @Column(name = "feedback_improvement")
    private String feedbackImprovement;

    @Column(name = "rating")
    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Events event;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}