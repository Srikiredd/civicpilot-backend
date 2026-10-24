package com.civicpilot.service;

import com.civicpilot.dto.LoginRequest;
import com.civicpilot.dto.LoginResponse;
import com.civicpilot.dto.RegisterRequest;
import com.civicpilot.entity.Role;
import com.civicpilot.entity.User;
import com.civicpilot.repository.UserRepository;
import com.civicpilot.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    // Constructor Injection
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;

        this.jwtService = jwtService;
    }

    // =========================
    // REGISTER
    // =========================

    public User register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException(
                    "Email already registered");
        }

        User user = new User();

        user.setName(request.getName());

        user.setEmail(request.getEmail());

        // Encrypt password using BCrypt
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword())
        );

        // Normal users are registered as CITIZEN
        user.setRole(Role.CITIZEN);

        // Phase 10 - Save phone number
        user.setPhoneNumber(
                request.getPhoneNumber());

        // Save user into database
        return userRepository.save(user);
    }

    // =========================
    // LOGIN
    // =========================

    public LoginResponse login(LoginRequest request) {

        // Find user by email
        User user =
                userRepository
                        .findByEmail(
                                request.getEmail())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid email or password"));

        // Verify entered password
        // against BCrypt password
        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {

            throw new RuntimeException(
                    "Invalid email or password");
        }

        // Generate JWT
        String token =
                jwtService.generateToken(user);

        // Return JWT + role
        return new LoginResponse(
                token,
                "Bearer",
                user.getRole()
        );
    }
}