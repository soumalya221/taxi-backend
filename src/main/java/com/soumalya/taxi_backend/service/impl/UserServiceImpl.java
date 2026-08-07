package com.soumalya.taxi_backend.service.impl;

import com.soumalya.taxi_backend.dto.request.LoginRequest;
import com.soumalya.taxi_backend.dto.request.RegisterRequest;
import com.soumalya.taxi_backend.dto.response.LoginResponse;
import com.soumalya.taxi_backend.dto.response.RegisterResponse;
import com.soumalya.taxi_backend.dto.response.UserProfileResponse;
import com.soumalya.taxi_backend.entity.User;
import com.soumalya.taxi_backend.exception.EmailAlreadyExistsException;
import com.soumalya.taxi_backend.exception.PhoneAlreadyExistsException;
import com.soumalya.taxi_backend.repository.UserRepository;
import com.soumalya.taxi_backend.security.JwtService;
import com.soumalya.taxi_backend.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new PhoneAlreadyExistsException("Phone already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("CUSTOMER");

        User savedUser = userRepository.save(user);
        return RegisterResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .role(savedUser.getRole())
                .message("Registration Successful")
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Authenticated user was not found"));

        return LoginResponse.builder()
                .success(true)
                .message("Login Successful")
                .token(jwtService.generateToken(user.getEmail()))
                .type("Bearer")
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public UserProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }
}
