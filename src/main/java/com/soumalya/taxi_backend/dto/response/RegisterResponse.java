package com.soumalya.taxi_backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String message;
}