package com.soumalya.taxi_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DriverLocationRequest {

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;
}