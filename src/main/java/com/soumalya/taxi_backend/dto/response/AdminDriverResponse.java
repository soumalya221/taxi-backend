package com.soumalya.taxi_backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDriverResponse {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String licenseNumber;

    private Integer experience;

    private Double rating;

    private String status;

    private Double currentLatitude;

    private Double currentLongitude;
}