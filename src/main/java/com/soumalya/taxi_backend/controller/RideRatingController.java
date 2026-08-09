package com.soumalya.taxi_backend.controller;

import com.soumalya.taxi_backend.dto.request.RideRatingRequest;
import com.soumalya.taxi_backend.dto.response.RideRatingResponse;
import com.soumalya.taxi_backend.service.RideRatingService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class RideRatingController {

    private final RideRatingService rideRatingService;

    public RideRatingController(
            RideRatingService rideRatingService) {

        this.rideRatingService =
                rideRatingService;
    }

    // ======================================================
    // SUBMIT RATING
    // ======================================================

    @PostMapping("/rides/{rideId}")
    public RideRatingResponse submitRating(
            Authentication authentication,
            @PathVariable Long rideId,
            @Valid @RequestBody RideRatingRequest request) {

        return rideRatingService.submitRating(
                authentication.getName(),
                rideId,
                request
        );
    }

    // ======================================================
    // GET RATING
    // ======================================================

    @GetMapping("/rides/{rideId}")
    public RideRatingResponse getRating(
            Authentication authentication,
            @PathVariable Long rideId) {

        return rideRatingService.getRating(
                authentication.getName(),
                rideId
        );
    }
}