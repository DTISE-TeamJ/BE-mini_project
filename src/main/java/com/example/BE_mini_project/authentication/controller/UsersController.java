package com.example.BE_mini_project.authentication.controller;

import com.example.BE_mini_project.authentication.dto.UserProfileDTO;
import com.example.BE_mini_project.authentication.dto.UserUpdateDTO;
import com.example.BE_mini_project.authentication.service.UsersService;
import com.example.BE_mini_project.response.CustomResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {
    private final UsersService usersService;
    private final ObjectMapper objectMapper;

    public UsersController(UsersService usersService, ObjectMapper objectMapper) {
        this.usersService = usersService;
        this.objectMapper = objectMapper;
    }

    @PutMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CustomResponse<UserUpdateDTO>> updateUser(
            @PathVariable Long userId,
            @RequestParam(value = "profilePicture", required = false) MultipartFile file,
            @RequestParam("userData") String userData) throws JsonProcessingException {

        UserUpdateDTO updateDTO = objectMapper.readValue(userData, UserUpdateDTO.class);

        UserUpdateDTO updatedUser = usersService.updateUser(userId, file, updateDTO);

        CustomResponse<UserUpdateDTO> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Updated",
                "User updated successfully",
                updatedUser
        );

        return customResponse.toResponseEntity();
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<CustomResponse<UserProfileDTO>> getUserProfile(@PathVariable Long userId) {
        UserProfileDTO userProfile = usersService.getUserProfile(userId);

        CustomResponse<UserProfileDTO> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "User profile fetched successfully",
                userProfile
        );

        return customResponse.toResponseEntity();
    }
}
