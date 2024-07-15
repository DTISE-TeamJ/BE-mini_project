package com.example.BE_mini_project.authentication.mapper;

import com.example.BE_mini_project.authentication.dto.UserProfileDTO;
import com.example.BE_mini_project.authentication.dto.UserUpdateDTO;
import com.example.BE_mini_project.authentication.model.Users;

public class UserMapper {
    public static void updateUserFromDTO(Users user, UserUpdateDTO dto) {
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getName() != null && !dto.getName().isEmpty()) {
            user.setName(dto.getName());
        }
        if (dto.getProfilePicture() != null && !dto.getProfilePicture().isEmpty()) {
            user.setProfilePicture(dto.getProfilePicture());
        }
    }

    public static UserUpdateDTO toDTO(Users user) {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setProfilePicture(user.getProfilePicture());
        return dto;
    }

    public static UserProfileDTO toProfileDTO(Users user, Integer totalPoints) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setReferralCode(user.getReferralCode());
        dto.setTotalPoints(totalPoints);
        return dto;
    }
}