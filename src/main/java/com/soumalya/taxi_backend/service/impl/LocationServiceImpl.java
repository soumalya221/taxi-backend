package com.soumalya.taxi_backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soumalya.taxi_backend.config.OpenRouteServiceConfig;
import com.soumalya.taxi_backend.service.LocationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final OpenRouteServiceConfig config;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public LocationServiceImpl(OpenRouteServiceConfig config) {
        this.config = config;
        this.restClient = RestClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<LocationResult> searchLocation(String query) {

        if (query == null || query.isBlank()) {
            return List.of();
        }

        try {

            String response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/pelias/v1/autocomplete")
                            .scheme("https")
                            .host("api.heigit.org")
                            .queryParam("text", query.trim())
                            .queryParam("size", 5)
                            .build())
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            config.getApiKey()
                    )
                    .retrieve()
                    .body(String.class);

            JsonNode features = objectMapper
                    .readTree(response)
                    .path("features");

            if (!features.isArray() || features.isEmpty()) {
                return List.of();
            }

            List<LocationResult> locations = new ArrayList<>();

            for (JsonNode feature : features) {

                JsonNode properties = feature.path("properties");

                JsonNode coordinates = feature
                        .path("geometry")
                        .path("coordinates");

                if (!coordinates.isArray() || coordinates.size() < 2) {
                    continue;
                }

                String name = properties
                        .path("name")
                        .asText("");

                String address = properties
                        .path("label")
                        .asText("");

                if (name.isBlank()) {
                    name = address;
                }

                if (address.isBlank()) {
                    address = name;
                }

                if (name.isBlank()) {
                    continue;
                }

                double longitude = coordinates
                        .get(0)
                        .asDouble();

                double latitude = coordinates
                        .get(1)
                        .asDouble();

                locations.add(
                        new LocationResult(
                                name,
                                address,
                                latitude,
                                longitude
                        )
                );

                if (locations.size() == 5) {
                    break;
                }
            }

            return locations;

        } catch (RestClientResponseException e) {

            System.out.println(
                    "HeiGIT API error: "
                            + e.getStatusCode()
                            + " "
                            + e.getResponseBodyAsString()
            );

            if (e.getStatusCode().value() == 429) {

                throw new ResponseStatusException(
                        HttpStatus.TOO_MANY_REQUESTS,
                        "Location search is temporarily rate limited"
                );
            }

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Location search service is unavailable"
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Location search service is unavailable",
                    e
            );
        }
    }
}