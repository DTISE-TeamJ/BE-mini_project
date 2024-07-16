package com.example.BE_mini_project.authentication.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import com.example.BE_mini_project.authentication.dto.RegistrationDTO;
import com.example.BE_mini_project.authentication.exception.*;
import com.example.BE_mini_project.authentication.model.Discount;
import com.example.BE_mini_project.authentication.model.Point;
import com.example.BE_mini_project.authentication.repository.*;
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

import java.security.SecureRandom;

@Service
@Transactional
public class AuthenticationService {

    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final AuthRedisRepository authRedisRepository;

    private final PointRepository pointRepository;

    private final DiscountRepository discountRepository;


    public AuthenticationService(UsersRepository usersRepository, RolesRepository rolesRepository,
                                 PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                                 TokenService tokenService, AuthRedisRepository authRedisRepository,
                                 PointRepository pointRepository, DiscountRepository discountRepository
                                 ) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.authRedisRepository = authRedisRepository;
        this.pointRepository = pointRepository;
        this.discountRepository = discountRepository;
    }

    public Users registerUser(RegistrationDTO newUserDto) {
        String email = newUserDto.getEmail();
        String password = newUserDto.getPassword();

        Optional<Users> userEmailOptional = usersRepository.findByEmail(email);
        Optional<Users> userUsernameOptional = usersRepository.findByUsername(newUserDto.getUsername());

        if (userUsernameOptional.isPresent()) {
            throw new UsernameException("Username already exists, please choose another one !");
        }

        if (userEmailOptional.isPresent()) {
            throw new EmailException("Email already exists, please choose another one !");
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

        if (isAdmin) {
            newUser.setReferralCode(null);
        }

        if (!isAdmin) {
            String referralCode = generateReferralCode(newUserDto.getUsername());
            newUser.setReferralCode(referralCode);
        }

        newUser = usersRepository.save(newUser);

// //        limit only 50
//        if (!isAdmin && newUserDto.getReferralCode() != null) {
//            Optional<Users> inviterOptional = usersRepository.findByReferralCode(newUserDto.getReferralCode());
//            if (inviterOptional.isPresent()) {
//                Users inviter = inviterOptional.get();
//                if (inviter.getPoint() < 50) { // Limit referral usage to 50 times (10 points each)
//                    inviter.setPoint(inviter.getPoint() + 10000);
//                    usersRepository.save(inviter);
//                } else {
//                    newUser.setReferralCode(null);
//                    usersRepository.save(newUser);
//                }
//            }
//        }

        if (!isAdmin && newUserDto.getReferralCode() != null && !newUserDto.getReferralCode().trim().isEmpty()) {
            Optional<Users> inviterOptional = usersRepository.findByReferralCode(newUserDto.getReferralCode());
            if (inviterOptional.isPresent()) {
                Users inviter = inviterOptional.get();

                Discount newDiscount = new Discount();
                newDiscount.setUser(newUser);
                newDiscount.setHasDiscount(true);
                discountRepository.save(newDiscount);

                Point newPoint = new Point();
                newPoint.setInvitee(newUser);
                newPoint.setInviter(inviter);
                newPoint.setPoints(10000);
                pointRepository.save(newPoint);
            } else {
                throw new ReferralCodeException("Referral code is not valid!");
            }
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