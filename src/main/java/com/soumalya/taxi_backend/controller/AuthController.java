package com.soumalya.taxi_backend.controller;

import com.soumalya.taxi_backend.dto.request.LoginRequest;
import com.soumalya.taxi_backend.dto.request.RegisterRequest;
import com.soumalya.taxi_backend.dto.response.LoginResponse;
import com.soumalya.taxi_backend.dto.response.RegisterResponse;
import com.soumalya.taxi_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Register API
    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    // Login API
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }
}