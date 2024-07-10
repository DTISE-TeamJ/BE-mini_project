package com.example.BE_mini_project.authentication.controller;

import com.example.BE_mini_project.authentication.exception.*;
import com.example.BE_mini_project.authentication.helper.Claims;
import com.example.BE_mini_project.authentication.repository.BlacklistAuthRedisRepository;
import com.example.BE_mini_project.authentication.repository.UsersRepository;

import com.example.BE_mini_project.response.CustomResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.BE_mini_project.authentication.model.Users;
import com.example.BE_mini_project.authentication.dto.LoginResponseDTO;
import com.example.BE_mini_project.authentication.dto.RegistrationDTO;
import com.example.BE_mini_project.authentication.service.AuthenticationService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    private final BlacklistAuthRedisRepository blacklistAuthRedisRepository;

    @PostMapping("/register")
    public ResponseEntity<CustomResponse<Users>> registerUser(@RequestBody RegistrationDTO body) {
        try {
            Users user = authenticationService.registerUser(body);
            CustomResponse<Users> response = new CustomResponse<>(
                    HttpStatus.CREATED,
                    "Success",
                    "User registered successfully",
                    user
            );

            return response.toResponseEntity();
        } catch (UsernameException e) {
            CustomResponse<Users> errorResponse = new CustomResponse<>(
                    HttpStatus.BAD_REQUEST,
                    "Error",
                    "Username is already taken, please choose another one",
                    null
            );
            return errorResponse.toResponseEntity();
        } catch (EmailException e) {
            CustomResponse<Users> errorResponse = new CustomResponse<>(
                    HttpStatus.BAD_REQUEST,
                    "Error",
                    "Email is already taken, please choose another one",
                    null
            );
            return errorResponse.toResponseEntity();
        } catch (ReferralCodeException e) {
            CustomResponse<Users> errorResponse = new CustomResponse<>(
                    HttpStatus.BAD_REQUEST,
                    "Error",
                    "Invalid referral code",
                    null
            );
            return errorResponse.toResponseEntity();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<LoginResponseDTO>> loginUser(@RequestBody RegistrationDTO body, HttpServletResponse response) {
        try {
            LoginResponseDTO loginResponse = authenticationService.loginUser(body.getUsername(), body.getPassword());

            if (loginResponse.getJwt() != null) {
                Cookie cookie = new Cookie("jwt", loginResponse.getJwt());
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60);
                response.addCookie(cookie);
            }

            CustomResponse<LoginResponseDTO> customResponse = new CustomResponse<>(
                    HttpStatus.OK,
                    "Success",
                    "Login successful",
                    loginResponse
            );
            return customResponse.toResponseEntity();
        } catch (PasswordException e) {
            CustomResponse<LoginResponseDTO> errorResponse = new CustomResponse<>(
                    HttpStatus.UNAUTHORIZED,
                    "Error",
                    "Password is incorrect",
                    null
            );
            return errorResponse.toResponseEntity();
        } catch (UsernameException e) {
            CustomResponse<LoginResponseDTO> errorResponse = new CustomResponse<>(
                    HttpStatus.UNAUTHORIZED,
                    "Error",
                    "Username is incorrect",
                    null
            );
            return errorResponse.toResponseEntity();
        } catch (AccountNotRegisteredException e) {
            CustomResponse<LoginResponseDTO> errorResponse = new CustomResponse<>(
                    HttpStatus.UNAUTHORIZED,
                    "Error",
                    "Account is not registered",
                    null
            );
            return errorResponse.toResponseEntity();
        } catch (Exception e) {
            CustomResponse<LoginResponseDTO> errorResponse = new CustomResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error",
                    "An unexpected error occurred",
                    null
            );
            return errorResponse.toResponseEntity();
        }
    }

    @Autowired
    public AuthenticationController(BlacklistAuthRedisRepository blacklistAuthRedisRepository) {
        this.blacklistAuthRedisRepository = blacklistAuthRedisRepository;
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
//        String token = extractJwtFromCookies(request);
//
//        if (token != null) {
//            blacklistAuthRedisRepository.blacklistToken(token);
//        }
//
//        SecurityContextHolder.clearContext();
//
//        // Clear the JWT cookie
//        Cookie cookie = new Cookie("jwt", null);
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge(0);
//        cookie.setPath("/");
//        response.addCookie(cookie);

        SecurityContextHolder.clearContext();

        // Clear the JWT cookie
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/profile")
    public Map<String, Object> getProfile() {
        return Claims.getClaimsFromJwt();
    }

    private String extractJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}