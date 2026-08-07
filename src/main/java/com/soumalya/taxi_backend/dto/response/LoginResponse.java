package com.soumalya.taxi_backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private boolean success;
    private String message;
    private String email;
    private String role;
    private String token;
    private String type;
}