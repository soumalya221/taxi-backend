package com.soumalya.taxi_backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RideResponse {

    private Long id;

    private String customer;

    private String driver;

    private String vehicle;

    private String pickupLocation;

    private String dropLocation;

    private Double distance;

    private Double durationMinutes;

    private Double fare;

    private String status;

}
