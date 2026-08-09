package com.soumalya.taxi_backend.service;

import com.soumalya.taxi_backend.dto.request.RideRatingRequest;
import com.soumalya.taxi_backend.dto.response.RideRatingResponse;

public interface RideRatingService {

    RideRatingResponse submitRating(
            String customerEmail,
            Long rideId,
            RideRatingRequest request
    );

    RideRatingResponse getRating(
            String customerEmail,
            Long rideId
    );
}