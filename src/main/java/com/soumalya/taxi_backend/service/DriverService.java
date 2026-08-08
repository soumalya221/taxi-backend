package com.soumalya.taxi_backend.service;

import com.soumalya.taxi_backend.dto.request.DriverLocationRequest;
import com.soumalya.taxi_backend.dto.request.DriverRegisterRequest;
import com.soumalya.taxi_backend.dto.response.DriverResponse;

public interface DriverService {

    DriverResponse registerDriver(String email,
                                  DriverRegisterRequest request);

    DriverResponse updateLocation(String email,
                                  DriverLocationRequest request);
}