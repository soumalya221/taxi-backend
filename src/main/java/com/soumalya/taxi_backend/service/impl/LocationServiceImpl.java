package com.soumalya.taxi_backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soumalya.taxi_backend.config.OpenRouteServiceConfig;
import com.soumalya.taxi_backend.service.LocationService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class LocationServiceImpl implements LocationService {

    private final OpenRouteServiceConfig config;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public LocationServiceImpl(OpenRouteServiceConfig config) {

        this.config = config;

        this.restClient = RestClient.builder()
                .baseUrl(config.getGeocodeUrl())
                .build();

        this.objectMapper = new ObjectMapper();
    }

    @Override
    public LocationResult searchLocation(String query) {

        try {

            String response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("api_key", config.getApiKey())
                            .queryParam("text", query)
                            .queryParam("size", 1)
                            .build())
                    .retrieve()
                    .body(String.class);

            JsonNode root =
                    objectMapper.readTree(response);

            JsonNode features =
                    root.path("features");

            if (!features.isArray()
                    || features.isEmpty()) {

                throw new RuntimeException(
                        "Location not found: " + query
                );
            }

            JsonNode feature =
                    features.get(0);

            String name =
                    feature.path("properties")
                            .path("label")
                            .asText(query);

            JsonNode coordinates =
                    feature.path("geometry")
                            .path("coordinates");

            if (!coordinates.isArray()
                    || coordinates.size() < 2) {

                throw new RuntimeException(
                        "Invalid coordinates returned for: " + query
                );
            }

            double longitude =
                    coordinates.get(0).asDouble();

            double latitude =
                    coordinates.get(1).asDouble();

            return new LocationResult(
                    name,
                    latitude,
                    longitude
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to search location: " + query,
                    e
            );
        }
    }
}