package com.example.BE_mini_project.authentication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateDTO {
    private String email;
    private String name;
    private String profilePicture;

}
