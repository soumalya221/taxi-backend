package com.soumalya.taxi_backend.dto.request;

import com.soumalya.taxi_backend.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookRideRequest {

    @NotBlank
    private String pickupLocation;

    @NotBlank
    private String dropLocation;

    private Double pickupLatitude;

    private Double pickupLongitude;

    private Double dropLatitude;

    private Double dropLongitude;

    @NotNull
    private VehicleType vehicleType;

}
