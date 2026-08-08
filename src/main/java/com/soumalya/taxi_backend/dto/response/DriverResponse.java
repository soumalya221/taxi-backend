package com.soumalya.taxi_backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverResponse {

    private Long id;
    private String name;
    private String email;
    private String licenseNumber;
    private Integer experience;
    private Double rating;
    private String status;
}