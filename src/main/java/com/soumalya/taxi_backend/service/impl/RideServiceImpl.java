package com.soumalya.taxi_backend.service.impl;

import com.soumalya.taxi_backend.dto.request.BookRideRequest;
import com.soumalya.taxi_backend.dto.response.RideResponse;
import com.soumalya.taxi_backend.entity.Driver;
import com.soumalya.taxi_backend.entity.Ride;
import com.soumalya.taxi_backend.entity.User;
import com.soumalya.taxi_backend.entity.Vehicle;
import com.soumalya.taxi_backend.enums.DriverStatus;
import com.soumalya.taxi_backend.enums.RideStatus;
import com.soumalya.taxi_backend.enums.VehicleType;
import com.soumalya.taxi_backend.repository.DriverRepository;
import com.soumalya.taxi_backend.repository.RideRepository;
import com.soumalya.taxi_backend.repository.UserRepository;
import com.soumalya.taxi_backend.repository.VehicleRepository;
import com.soumalya.taxi_backend.service.OpenRouteService;
import com.soumalya.taxi_backend.service.RideService;
import com.soumalya.taxi_backend.util.DistanceCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideServiceImpl implements RideService {

    private static final BigDecimal BASE_FARE =
            BigDecimal.valueOf(50);

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final OpenRouteService openRouteService;

    public RideServiceImpl(
            RideRepository rideRepository,
            UserRepository userRepository,
            DriverRepository driverRepository,
            VehicleRepository vehicleRepository,
            OpenRouteService openRouteService) {

        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.openRouteService = openRouteService;
    }

    // ======================================================
    // BOOK RIDE
    // ======================================================

    @Override
    public RideResponse bookRide(
            String email,
            BookRideRequest request) {

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Ride ride = new Ride();

        ride.setCustomer(customer);

        ride.setPickupLocation(
                request.getPickupLocation()
        );

        ride.setDropLocation(
                request.getDropLocation()
        );

        ride.setPickupLatitude(
                request.getPickupLatitude()
        );

        ride.setPickupLongitude(
                request.getPickupLongitude()
        );

        ride.setDropLatitude(
                request.getDropLatitude()
        );

        ride.setDropLongitude(
                request.getDropLongitude()
        );

        ride.setVehicleType(
                request.getVehicleType()
        );

        // ==================================================
        // OPENROUTESERVICE ROAD ROUTE
        // ==================================================

        OpenRouteService.RouteResult route =
                openRouteService.getRoute(
                        request.getPickupLatitude(),
                        request.getPickupLongitude(),
                        request.getDropLatitude(),
                        request.getDropLongitude()
                );

        double distanceKm =
                roundTwoDecimals(
                        route.distanceKm()
                );

        double durationMinutes =
                roundTwoDecimals(
                        route.durationMinutes()
                );

        ride.setDistance(distanceKm);

        ride.setDurationMinutes(
                durationMinutes
        );

        // ==================================================
        // ESTIMATED FARE
        // ==================================================

        double estimatedFare =
                calculateFare(
                        distanceKm,
                        request.getVehicleType()
                );

        ride.setFare(estimatedFare);

        ride.setStatus(
                RideStatus.REQUESTED
        );

        Ride savedRide =
                rideRepository.save(ride);

        return mapToResponse(savedRide);
    }

    // ======================================================
    // ACCEPT RIDE
    // ======================================================

    @Override
    public RideResponse acceptRide(Long rideId) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException("Ride not found"));

        if (ride.getStatus() != RideStatus.REQUESTED) {

            throw new RuntimeException(
                    "Ride is not available for acceptance"
            );
        }

        List<Driver> drivers =
                driverRepository.findByStatus(
                        DriverStatus.ONLINE
                );

        if (drivers.isEmpty()) {

            throw new RuntimeException(
                    "No available drivers"
            );
        }

        // ==================================================
        // FIND NEAREST DRIVER
        // ==================================================

        Driver nearestDriver = null;

        double shortestDistance =
                Double.MAX_VALUE;

        for (Driver driver : drivers) {

            if (driver.getCurrentLatitude() == null
                    || driver.getCurrentLongitude() == null) {

                continue;
            }

            double distance =
                    DistanceCalculator.calculateDistance(
                            ride.getPickupLatitude(),
                            ride.getPickupLongitude(),
                            driver.getCurrentLatitude(),
                            driver.getCurrentLongitude()
                    );

            if (distance < shortestDistance) {

                shortestDistance = distance;
                nearestDriver = driver;
            }
        }

        if (nearestDriver == null) {

            throw new RuntimeException(
                    "No driver has shared location"
            );
        }

        // ==================================================
        // FIND DRIVER VEHICLE
        // ==================================================

        Vehicle vehicle =
                vehicleRepository
                        .findByDriverAndVehicleType(
                                nearestDriver,
                                ride.getVehicleType()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Vehicle not found"
                                )
                        );

        ride.setDriver(nearestDriver);
        ride.setVehicle(vehicle);
        ride.setStatus(RideStatus.ACCEPTED);

        nearestDriver.setStatus(
                DriverStatus.ON_RIDE
        );

        driverRepository.save(nearestDriver);

        Ride updatedRide =
                rideRepository.save(ride);

        return mapToResponse(updatedRide);
    }

    // ======================================================
    // START RIDE
    // ======================================================

    @Override
    public RideResponse startRide(Long rideId) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException("Ride not found"));

        if (ride.getStatus() != RideStatus.ACCEPTED) {

            throw new RuntimeException(
                    "Ride is not accepted"
            );
        }

        ride.setStatus(
                RideStatus.STARTED
        );

        Ride updatedRide =
                rideRepository.save(ride);

        return mapToResponse(updatedRide);
    }

    // ======================================================
    // COMPLETE RIDE
    // ======================================================

    @Override
    public RideResponse completeRide(Long rideId) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException("Ride not found"));

        if (ride.getStatus() != RideStatus.STARTED) {

            throw new RuntimeException(
                    "Ride has not started"
            );
        }

        Driver driver =
                ride.getDriver();

        // ==================================================
        // FINAL FARE
        // ==================================================

        double finalFare =
                calculateFare(
                        ride.getDistance(),
                        ride.getVehicleType()
                );

        ride.setFare(finalFare);

        ride.setStatus(
                RideStatus.COMPLETED
        );

        // Driver becomes available again
        driver.setStatus(
                DriverStatus.OFFLINE
        );

        driverRepository.save(driver);

        Ride updatedRide =
                rideRepository.save(ride);

        return mapToResponse(updatedRide);
    }

    // ======================================================
    // CUSTOMER HISTORY
    // ======================================================

    @Override
    public List<RideResponse> getCustomerHistory(
            String email) {

        User customer =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        return rideRepository
                .findByCustomer(customer)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ======================================================
    // DRIVER HISTORY
    // ======================================================

    @Override
    public List<RideResponse> getDriverHistory(
            String email) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        Driver driver =
                driverRepository.findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Driver not found"
                                )
                        );

        return rideRepository
                .findByDriver(driver)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ======================================================
    // FARE CALCULATION
    // ======================================================

    private double calculateFare(
            double distanceKm,
            VehicleType vehicleType) {

        BigDecimal perKmRate = switch (vehicleType) {
            case HATCHBACK -> BigDecimal.valueOf(12);
            case SEDAN -> BigDecimal.valueOf(15);
            case SUV -> BigDecimal.valueOf(20);
            default -> throw new IllegalArgumentException(
                    "Unsupported vehicle type for booking: " +
                            vehicleType
            );
        };

        return BASE_FARE
                .add(
                        BigDecimal.valueOf(distanceKm)
                                .multiply(perKmRate)
                )
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    // ======================================================
    // ROUND TO 2 DECIMAL PLACES
    // ======================================================

    private double roundTwoDecimals(
            double value) {

        return Math.round(
                value * 100.0
        ) / 100.0;
    }

    // ======================================================
    // RESPONSE MAPPER
    // ======================================================

    @Override
    public List<RideResponse> getAvailableRides() {

        return rideRepository
                .findByStatus(RideStatus.REQUESTED)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private RideResponse mapToResponse(
            Ride ride) {

        return RideResponse.builder()

                .id(ride.getId())

                .customer(
                        ride.getCustomer()
                                .getEmail()
                )

                .driver(
                        ride.getDriver() == null
                                ? null
                                : ride.getDriver()
                                        .getUser()
                                        .getEmail()
                )

                .driverId(
                        ride.getDriver() == null
                                ? null
                                : ride.getDriver()
                                        .getId()
                )

                .vehicle(
                        ride.getVehicle() == null
                                ? null
                                : ride.getVehicle()
                                        .getVehicleNumber()
                )

                .vehicleType(
                        ride.getVehicleType() == null
                                ? null
                                : ride.getVehicleType().name()
                )

                .pickupLocation(
                        ride.getPickupLocation()
                )

                .dropLocation(
                        ride.getDropLocation()
                )

                .distance(
                        ride.getDistance()
                )

                .durationMinutes(
                        ride.getDurationMinutes()
                )

                .fare(
                        ride.getFare()
                )

                .status(
                        ride.getStatus().name()
                )

                .build();
    }
}
