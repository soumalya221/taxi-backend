package com.soumalya.taxi_backend.service;

import java.util.List;

public interface LocationService {

    List<LocationResult> searchLocation(String query);

    record LocationResult(
            String name,
            String address,
            double latitude,
            double longitude
    ) {
    }
}
