package com.soumalya.taxi_backend.service;

import com.soumalya.taxi_backend.dto.request.VehicleRegisterRequest;
import com.soumalya.taxi_backend.dto.response.VehicleResponse;

public interface VehicleService {

    VehicleResponse registerVehicle(
            String email,
            VehicleRegisterRequest request
    );

}