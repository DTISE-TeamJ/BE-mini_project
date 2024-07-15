package com.example.BE_mini_project.authentication.service;

import com.example.BE_mini_project.authentication.dto.UserProfileDTO;
import com.example.BE_mini_project.authentication.dto.UserUpdateDTO;
import com.example.BE_mini_project.authentication.mapper.UserMapper;
import com.example.BE_mini_project.authentication.model.Users;
import com.example.BE_mini_project.events.service.CloudinaryService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.BE_mini_project.authentication.repository.UsersRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class UsersService implements UserDetailsService {


    private final UsersRepository usersRepository;
    private final CloudinaryService cloudinaryService;
    private final PointService pointService;

    public UsersService(UsersRepository usersRepository, CloudinaryService cloudinaryService, PointService pointService) {
        this.usersRepository = usersRepository;
        this.cloudinaryService = cloudinaryService;
        this.pointService = pointService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("In the user details service");
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User is not valid"));
    }

    @Transactional
    public UserUpdateDTO updateUser(Long userId, MultipartFile file, UserUpdateDTO updateDTO) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (file != null && !file.isEmpty()) {
            Map<String, String> uploadResult = cloudinaryService.uploadFile(file);
            String imageUrl = uploadResult.get("url");
            user.setProfilePicture(imageUrl);
        }

        UserMapper.updateUserFromDTO(user, updateDTO);

        Users updatedUser = usersRepository.save(user);
        return UserMapper.toDTO(updatedUser);
    }

    public UserProfileDTO getUserProfile(Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Integer totalPoints = pointService.sumPointsByUserId(userId);

        return UserMapper.toProfileDTO(user, totalPoints);
    }

}