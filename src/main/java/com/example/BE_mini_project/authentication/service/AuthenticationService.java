package com.example.BE_mini_project.authentication.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import com.example.BE_mini_project.authentication.dto.RegistrationDTO;
import com.example.BE_mini_project.authentication.exception.AccountNotRegisteredException;
import com.example.BE_mini_project.authentication.exception.PasswordException;
import com.example.BE_mini_project.authentication.exception.UsernameException;
import com.example.BE_mini_project.authentication.repository.AuthRedisRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.BE_mini_project.authentication.model.Users;
import com.example.BE_mini_project.authentication.model.Roles;
import com.example.BE_mini_project.authentication.dto.LoginResponseDTO;
import com.example.BE_mini_project.authentication.repository.RolesRepository;
import com.example.BE_mini_project.authentication.repository.UsersRepository;
import java.security.SecureRandom;


@Service
@Transactional
public class AuthenticationService {

    private UsersRepository usersRepository;

    private RolesRepository rolesRepository;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private TokenService tokenService;

    private AuthRedisRepository authRedisRepository;

    public AuthenticationService(UsersRepository usersRepository, RolesRepository rolesRepository,
                                 PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                                 TokenService tokenService, AuthRedisRepository authRedisRepository) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.authRedisRepository = authRedisRepository;
    }

    public Users registerUser(RegistrationDTO newUserDto) {
        String email = newUserDto.getEmail();
        String password = newUserDto.getPassword();

        Optional<Users> userEmailOptional = usersRepository.findByEmail(email);
        Optional<Users> userUsernameOptional = usersRepository.findByUsername(newUserDto.getUsername());

        if (userEmailOptional.isPresent()) {
            throw new UsernameException("Email already exists, please choose another one !");
        }

        if (userUsernameOptional.isPresent()) {
            throw new UsernameException("Username already exists, please choose another one !");
        }

        String encodedPassword = passwordEncoder.encode(password);
        Set<Roles> authorities = new HashSet<>();
        boolean isAdmin = false;

        if (email.toLowerCase().contains("@admin")) {
            var adminRole = rolesRepository.findByAuthority("ADMIN");
            if (adminRole.isPresent()) {
                authorities.add(adminRole.get());
            } else {
                var defaultAdminRole = new Roles("ADMIN");
                rolesRepository.save(defaultAdminRole);
                authorities.add(defaultAdminRole);
            }
            isAdmin = true;
        } else {
            var userRole = rolesRepository.findByAuthority("USER");
            if (userRole.isPresent()) {
                authorities.add(userRole.get());
            } else {
                var defaultUserRole = new Roles("USER");
                rolesRepository.save(defaultUserRole);
                authorities.add(defaultUserRole);
            }
        }

        Users newUser = new Users();
        newUser.setUsername(newUserDto.getUsername());
        newUser.setEmail(newUserDto.getEmail());
        newUser.setPassword(encodedPassword);
        newUser.setAuthorities(authorities);

        if (!isAdmin) {
            String referralCode = generateReferralCode(newUserDto.getUsername());
            newUser.setReferralCode(referralCode);
        }

        newUser = usersRepository.save(newUser);

// //        limit only 50
//        if (!isAdmin && newUserDto.getReferralCode() != null) {
//            Optional<Users> referrerOptional = usersRepository.findByReferralCode(newUserDto.getReferralCode());
//            if (referrerOptional.isPresent()) {
//                Users referrer = referrerOptional.get();
//                if (referrer.getPoint() < 50) { // Limit referral usage to 5 times (10 points each)
//                    referrer.setPoint(referrer.getPoint() + 10);
//                    usersRepository.save(referrer);
//                } else {
//                    newUser.setReferralCode(null);
//                    usersRepository.save(newUser);
//                }
//            }
//        }

        if (!isAdmin && newUserDto.getReferralCode() != null) {
            Optional<Users> referrerOptional = usersRepository.findByReferralCode(newUserDto.getReferralCode());
            if (referrerOptional.isPresent()) {
                Users referrer = referrerOptional.get();
                referrer.setPoint(referrer.getPoint() + 10);
                usersRepository.save(referrer);
            }
        }

        if (isAdmin) {
            newUser.setReferralCode(null);
        }

        return usersRepository.findById(newUser.getId()).orElseThrow(() -> new RuntimeException("User not found"));
    }


    private String generateReferralCode(String username) {
        SecureRandom random = new SecureRandom();
        int randomNumber = random.nextInt(10000);
        return username.substring(0, Math.min(2, username.length())) + String.format("%04d", randomNumber);
    }

    public LoginResponseDTO loginUser(String identifier, String password) {
        Optional<Users> userOptionalByEmail = usersRepository.findByEmail(identifier);
        Optional<Users> userOptionalByUsername = usersRepository.findByUsername(identifier);

        Optional<Users> userOptional;
        if (userOptionalByEmail.isPresent()) {
            userOptional = userOptionalByEmail;
        } else if (userOptionalByUsername.isPresent()) {
            userOptional = userOptionalByUsername;
        } else {
            throw new UsernameException("Username or email is incorrect");
        }

        try {
            Users user = userOptional.get();

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new PasswordException("Password is incorrect");
            }

            String token = authRedisRepository.getJwtKey(user.getUsername());
            if (token != null) {
                return new LoginResponseDTO(user, token);
            }

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), password)
            );
            token = tokenService.generateJwt(auth);

            authRedisRepository.saveJwtKey(user.getUsername(), token);

            return new LoginResponseDTO(user, token);

        } catch (AuthenticationException e) {
            throw new AccountNotRegisteredException("Account is not registered");
        }
    }
}