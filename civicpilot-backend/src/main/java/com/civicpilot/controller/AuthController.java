package com.civicpilot.controller;

import com.civicpilot.dto.LoginRequest;
import com.civicpilot.dto.LoginResponse;
import com.civicpilot.dto.RegisterRequest;
import com.civicpilot.entity.User;
import com.civicpilot.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(
            @Valid @RequestBody RegisterRequest request) {

        User response = authService.register(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }
}