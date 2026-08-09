package com.soumalya.taxi_backend.controller;

import com.soumalya.taxi_backend.dto.request.BookRideRequest;
import com.soumalya.taxi_backend.dto.response.RideResponse;
import com.soumalya.taxi_backend.service.OpenRouteService;
import com.soumalya.taxi_backend.service.RideService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;
    private final OpenRouteService openRouteService;

    public RideController(
            RideService rideService,
            OpenRouteService openRouteService) {

        this.rideService = rideService;
        this.openRouteService = openRouteService;
    }

    @PostMapping("/book")
    public RideResponse bookRide(
            Authentication authentication,
            @Valid @RequestBody BookRideRequest request) {

        return rideService.bookRide(
                authentication.getName(),
                request
        );
    }

    @PatchMapping("/{rideId}/accept")
    public RideResponse acceptRide(
            @PathVariable Long rideId) {

        return rideService.acceptRide(rideId);
    }

    @PatchMapping("/{rideId}/start")
    public RideResponse startRide(
            @PathVariable Long rideId) {

        return rideService.startRide(rideId);
    }

    @PatchMapping("/{rideId}/complete")
    public RideResponse completeRide(
            @PathVariable Long rideId) {

        return rideService.completeRide(rideId);
    }

    @GetMapping("/history")
    public List<RideResponse> customerHistory(
            Authentication authentication) {

        return rideService.getCustomerHistory(
                authentication.getName()
        );
    }

    @GetMapping("/my-rides")
    public List<RideResponse> driverHistory(
            Authentication authentication) {

        return rideService.getDriverHistory(
                authentication.getName()
        );
    }

    @GetMapping("/available")
    public List<RideResponse> availableRides() {

        return rideService.getAvailableRides();
    }

    // ==========================================
    // ROUTE API
    // ==========================================

    @GetMapping("/route")
    public RouteResponse getRoute(
            @RequestParam double pickupLatitude,
            @RequestParam double pickupLongitude,
            @RequestParam double dropLatitude,
            @RequestParam double dropLongitude) {

        OpenRouteService.RouteResult result =
                openRouteService.getRoute(
                        pickupLatitude,
                        pickupLongitude,
                        dropLatitude,
                        dropLongitude
                );

        return new RouteResponse(
                result.distanceKm(),
                result.durationMinutes(),
                result.geometry()
        );
    }

    public record RouteResponse(
            double distanceKm,
            double durationMinutes,
            String geometry
    ) {
    }
}
