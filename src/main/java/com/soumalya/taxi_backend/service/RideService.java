package com.soumalya.taxi_backend.service;

import com.soumalya.taxi_backend.dto.request.BookRideRequest;
import com.soumalya.taxi_backend.dto.response.RideResponse;

import java.util.List;

public interface RideService {

    RideResponse bookRide(String email,
                          BookRideRequest request);

    RideResponse acceptRide(Long rideId);

    RideResponse startRide(Long rideId);

    RideResponse completeRide(Long rideId);

    List<RideResponse> getCustomerHistory(String email);

    List<RideResponse> getDriverHistory(String email);
}