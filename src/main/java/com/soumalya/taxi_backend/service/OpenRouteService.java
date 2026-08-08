package com.soumalya.taxi_backend.service;

public interface OpenRouteService {

    RouteResult getRoute(
            double pickupLatitude,
            double pickupLongitude,
            double dropLatitude,
            double dropLongitude
    );

    record RouteResult(
            double distanceKm,
            double durationMinutes
    ) {
    }
}