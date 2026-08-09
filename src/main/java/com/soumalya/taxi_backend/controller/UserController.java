package com.soumalya.taxi_backend.controller;

import com.soumalya.taxi_backend.dto.request.RegisterRequest;
import com.soumalya.taxi_backend.dto.response.RegisterResponse;
import com.soumalya.taxi_backend.dto.response.UserProfileResponse;
import com.soumalya.taxi_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public UserProfileResponse getProfile(Authentication authentication) {
        return userService.getProfile(authentication.getName());
    }

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }
}