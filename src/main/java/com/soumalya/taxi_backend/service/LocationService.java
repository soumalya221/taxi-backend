package com.soumalya.taxi_backend.service;

public interface LocationService {

    LocationResult searchLocation(String query);

    record LocationResult(
            String name,
            double latitude,
            double longitude
    ) {
    }
}