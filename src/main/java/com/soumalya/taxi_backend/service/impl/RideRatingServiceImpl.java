package com.soumalya.taxi_backend.service.impl;

import com.soumalya.taxi_backend.dto.request.RideRatingRequest;
import com.soumalya.taxi_backend.dto.response.RideRatingResponse;
import com.soumalya.taxi_backend.entity.Driver;
import com.soumalya.taxi_backend.entity.Ride;
import com.soumalya.taxi_backend.entity.RideRating;
import com.soumalya.taxi_backend.entity.User;
import com.soumalya.taxi_backend.enums.RideStatus;
import com.soumalya.taxi_backend.repository.DriverRepository;
import com.soumalya.taxi_backend.repository.RideRatingRepository;
import com.soumalya.taxi_backend.repository.RideRepository;
import com.soumalya.taxi_backend.repository.UserRepository;
import com.soumalya.taxi_backend.service.RideRatingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideRatingServiceImpl
        implements RideRatingService {

    private final RideRatingRepository rideRatingRepository;
    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final DriverRepository driverRepository;

    public RideRatingServiceImpl(
            RideRatingRepository rideRatingRepository,
            RideRepository rideRepository,
            UserRepository userRepository,
            DriverRepository driverRepository) {

        this.rideRatingRepository = rideRatingRepository;
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
    }

    // ======================================================
    // SUBMIT RATING
    // ======================================================

    @Override
    public RideRatingResponse submitRating(
            String customerEmail,
            Long rideId,
            RideRatingRequest request) {

        User customer =
                userRepository.findByEmail(customerEmail)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        Ride ride =
                rideRepository.findById(rideId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Ride not found"
                                )
                        );

        // --------------------------------------------------
        // VERIFY CUSTOMER
        // --------------------------------------------------

        if (!ride.getCustomer()
                .getId()
                .equals(customer.getId())) {

            throw new RuntimeException(
                    "You are not allowed to rate this ride"
            );
        }

        // --------------------------------------------------
        // VERIFY COMPLETED
        // --------------------------------------------------

        if (ride.getStatus() != RideStatus.COMPLETED) {

            throw new RuntimeException(
                    "Only completed rides can be rated"
            );
        }

        // --------------------------------------------------
        // VERIFY DRIVER
        // --------------------------------------------------

        Driver driver = ride.getDriver();

        if (driver == null) {

            throw new RuntimeException(
                    "No driver assigned to this ride"
            );
        }

        // --------------------------------------------------
        // PREVENT DUPLICATE RATING
        // --------------------------------------------------

        if (rideRatingRepository.existsByRide(ride)) {

            throw new RuntimeException(
                    "This ride has already been rated"
            );
        }

        // --------------------------------------------------
        // CREATE RATING
        // --------------------------------------------------

        RideRating rideRating =
                RideRating.builder()
                        .ride(ride)
                        .customer(customer)
                        .driver(driver)
                        .rating(request.getRating())
                        .comment(request.getComment())
                        .build();

        RideRating savedRating =
                rideRatingRepository.save(rideRating);

        // --------------------------------------------------
        // UPDATE DRIVER AVERAGE RATING
        // --------------------------------------------------

        updateDriverRating(driver);

        return mapToResponse(savedRating);
    }

    // ======================================================
    // GET RATING
    // ======================================================

    @Override
    public RideRatingResponse getRating(
            String customerEmail,
            Long rideId) {

        User customer =
                userRepository.findByEmail(customerEmail)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        Ride ride =
                rideRepository.findById(rideId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Ride not found"
                                )
                        );

        if (!ride.getCustomer()
                .getId()
                .equals(customer.getId())) {

            throw new RuntimeException(
                    "You are not allowed to view this rating"
            );
        }

        RideRating rating =
                rideRatingRepository.findByRide(ride)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Rating not found"
                                )
                        );

        return mapToResponse(rating);
    }

    // ======================================================
    // UPDATE DRIVER AVERAGE
    // ======================================================

    private void updateDriverRating(Driver driver) {

        List<RideRating> ratings =
                rideRatingRepository.findByDriver(driver);

        if (ratings.isEmpty()) {

            driver.setRating(5.0);

        } else {

            double total =
                    ratings.stream()
                            .mapToInt(
                                    RideRating::getRating
                            )
                            .sum();

            double average =
                    total / ratings.size();

            driver.setRating(
                    Math.round(
                            average * 100.0
                    ) / 100.0
            );
        }

        driverRepository.save(driver);
    }

    // ======================================================
    // RESPONSE MAPPER
    // ======================================================

    private RideRatingResponse mapToResponse(
            RideRating rating) {

        return RideRatingResponse.builder()

                .id(rating.getId())

                .rideId(
                        rating.getRide()
                                .getId()
                )

                .customer(
                        rating.getCustomer()
                                .getEmail()
                )

                .driver(
                        rating.getDriver()
                                .getUser()
                                .getEmail()
                )

                .rating(
                        rating.getRating()
                )

                .comment(
                        rating.getComment()
                )

                .build();
    }
}