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

            JsonNode root =
                    objectMapper.readTree(response);

            JsonNode routes =
                    root.path("routes");

            if (!routes.isArray()
                    || routes.isEmpty()) {

                throw new RuntimeException(
                        "No route found"
                );
            }

            JsonNode route =
                    routes.get(0);

            JsonNode summary =
                    route.path("summary");

            double distanceMeters =
                    summary.path("distance").asDouble();

            double durationSeconds =
                    summary.path("duration").asDouble();

            double distanceKm =
                    distanceMeters / 1000.0;

            double durationMinutes =
                    durationSeconds / 60.0;

            /*
             * OpenRouteService returns the route geometry
             * as an encoded polyline string.
             */
            String geometry =
                    route.path("geometry").asText();

            if (geometry == null
                    || geometry.isBlank()) {

                throw new RuntimeException(
                        "Route geometry not returned"
                );
            }

            return new RouteResult(
                    distanceKm,
                    durationMinutes,
                    geometry
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse OpenRouteService response",
                    e
            );
        }
    }
}