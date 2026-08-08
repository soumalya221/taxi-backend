package com.soumalya.taxi_backend.dto.request;

import com.soumalya.taxi_backend.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleRegisterRequest {

    @NotBlank
    private String vehicleNumber;

    @NotNull
    private VehicleType vehicleType;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotBlank
    private String color;

    @NotNull
    private Integer seatCapacity;

}