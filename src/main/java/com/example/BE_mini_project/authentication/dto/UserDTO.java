package com.example.BE_mini_project.authentication.dto;

import com.example.BE_mini_project.authentication.model.Users;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;

    public UserDTO(Users user) {
        this.id = Long.valueOf(user.getId());
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}