package com.example.BE_mini_project.authentication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileDTO {
    private String name;
    private String username;
    private String email;
    private String profilePicture;
    private String referralCode;
    private Integer totalPoints;

}