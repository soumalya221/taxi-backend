package com.soumalya.taxi_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenRouteServiceConfig {

    @Value("${openrouteservice.api.key}")
    private String apiKey;

    @Value("${openrouteservice.api.url}")
    private String apiUrl;

    @Value("${openrouteservice.geocode.url}")
    private String geocodeUrl;

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getGeocodeUrl() {
        return geocodeUrl;
    }
}