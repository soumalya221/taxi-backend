package com.soumalya.taxi_backend.service;

import com.soumalya.taxi_backend.dto.request.LoginRequest;
import com.soumalya.taxi_backend.dto.request.RegisterRequest;
import com.soumalya.taxi_backend.dto.response.LoginResponse;
import com.soumalya.taxi_backend.dto.response.RegisterResponse;
import com.soumalya.taxi_backend.dto.response.UserProfileResponse;

public interface UserService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    UserProfileResponse getProfile(String email);
}
