package com.example.BE_mini_project.authentication.services;

import com.example.BE_mini_project.authentication.dto.RegistrationDTO;
import com.example.BE_mini_project.authentication.exception.*;
import com.example.BE_mini_project.authentication.model.Users;
import com.example.BE_mini_project.authentication.repository.*;
import com.example.BE_mini_project.authentication.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        RegistrationDTO registrationDTO = new RegistrationDTO("username", "user@example.com", "password", null);
        Users user = new Users();
        user.setUsername("username");
        user.setEmail("user@example.com");

        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usersRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usersRepository.save(user)).thenReturn(user);

        Users registeredUser = authenticationService.registerUser(registrationDTO);

        assertNotNull(registeredUser);
        assertEquals("username", registeredUser.getUsername());
        assertEquals("user@example.com", registeredUser.getEmail());
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        RegistrationDTO registrationDTO = new RegistrationDTO("username", "user@example.com", "password", null);
        Users existingUser = new Users();
        existingUser.setEmail("user@example.com");

        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));

        assertThrows(EmailException.class, () -> authenticationService.registerUser(registrationDTO));
    }

    @Test
    public void testRegisterUser_UsernameAlreadyExists() {
        RegistrationDTO registrationDTO = new RegistrationDTO("username", "user@example.com", "password", null);
        Users existingUser = new Users();
        existingUser.setUsername("username");

        when(usersRepository.findByUsername(anyString())).thenReturn(Optional.of(existingUser));

        assertThrows(UsernameException.class, () -> authenticationService.registerUser(registrationDTO));
    }
}
