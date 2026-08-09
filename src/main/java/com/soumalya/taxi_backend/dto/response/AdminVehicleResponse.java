package com.soumalya.taxi_backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminVehicleResponse {

    private Long id;

    private String vehicleNumber;

    private String vehicleType;

    private String brand;

    private String model;

    private String color;

    private Integer seatCapacity;

    private String status;

    private Long driverId;

    private String driverName;

    private String driverEmail;
}