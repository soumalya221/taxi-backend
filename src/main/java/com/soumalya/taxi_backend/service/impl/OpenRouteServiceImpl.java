package com.soumalya.taxi_backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soumalya.taxi_backend.config.OpenRouteServiceConfig;
import com.soumalya.taxi_backend.service.OpenRouteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class OpenRouteServiceImpl implements OpenRouteService {

    private final OpenRouteServiceConfig config;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public OpenRouteServiceImpl(OpenRouteServiceConfig config) {
        this.config = config;
        this.restClient = RestClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public RouteResult getRoute(
            double pickupLatitude,
            double pickupLongitude,
            double dropLatitude,
            double dropLongitude) {

        String url = config.getApiUrl()
                + "/v2/directions/driving-car";

        Map<String, Object> requestBody = Map.of(
                "coordinates",
                new double[][]{
                        {
                                pickupLongitude,
                                pickupLatitude
                        },
                        {
                                dropLongitude,
                                dropLatitude
                        }
                }
        );

        String response = restClient.post()
                .uri(url)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        config.getApiKey()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);

        try {
            JsonNode root = objectMapper.readTree(response);

            JsonNode summary = root
                    .path("routes")
                    .get(0)
                    .path("summary");

            double distanceMeters =
                    summary.path("distance").asDouble();

            double durationSeconds =
                    summary.path("duration").asDouble();

            double distanceKm = distanceMeters / 1000.0;
            double durationMinutes = durationSeconds / 60.0;

            return new RouteResult(
                    distanceKm,
                    durationMinutes
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to parse OpenRouteService response",
                    e
            );
        }
    }
}