package com.soumalya.taxi_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
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

}