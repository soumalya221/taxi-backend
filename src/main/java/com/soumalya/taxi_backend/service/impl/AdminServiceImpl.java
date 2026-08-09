package com.soumalya.taxi_backend.service.impl;

import com.soumalya.taxi_backend.dto.response.AdminCustomerResponse;
import com.soumalya.taxi_backend.dto.response.AdminDashboardResponse;
import com.soumalya.taxi_backend.dto.response.AdminDriverResponse;
import com.soumalya.taxi_backend.dto.response.AdminVehicleResponse;
import com.soumalya.taxi_backend.dto.response.RideResponse;
import com.soumalya.taxi_backend.entity.Driver;
import com.soumalya.taxi_backend.entity.Ride;
import com.soumalya.taxi_backend.entity.User;
import com.soumalya.taxi_backend.entity.Vehicle;
import com.soumalya.taxi_backend.enums.RideStatus;
import com.soumalya.taxi_backend.repository.DriverRepository;
import com.soumalya.taxi_backend.repository.RideRepository;
import com.soumalya.taxi_backend.repository.UserRepository;
import com.soumalya.taxi_backend.repository.VehicleRepository;
import com.soumalya.taxi_backend.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final DriverRepository driverRepository;

    private final VehicleRepository vehicleRepository;

    private final RideRepository rideRepository;


    public AdminServiceImpl(
            UserRepository userRepository,
            DriverRepository driverRepository,
            VehicleRepository vehicleRepository,
            RideRepository rideRepository) {

        this.userRepository = userRepository;

        this.driverRepository = driverRepository;

        this.vehicleRepository = vehicleRepository;

        this.rideRepository = rideRepository;
    }


    // ======================================================
    // ADMIN DASHBOARD
    // ======================================================

    @Override
    public AdminDashboardResponse getDashboard() {

        long totalCustomers =
                userRepository.findAll()
                        .stream()
                        .filter(user ->
                                "CUSTOMER".equalsIgnoreCase(
                                        user.getRole()
                                )
                        )
                        .count();


        long totalDrivers =
                driverRepository.count();


        long totalVehicles =
                vehicleRepository.count();


        long totalRides =
                rideRepository.count();


        long requestedRides =
                rideRepository
                        .findByStatus(
                                RideStatus.REQUESTED
                        )
                        .size();


        long acceptedRides =
                rideRepository
                        .findByStatus(
                                RideStatus.ACCEPTED
                        )
                        .size();


        long startedRides =
                rideRepository
                        .findByStatus(
                                RideStatus.STARTED
                        )
                        .size();


        long completedRides =
                rideRepository
                        .findByStatus(
                                RideStatus.COMPLETED
                        )
                        .size();


        return AdminDashboardResponse.builder()

                .totalCustomers(
                        totalCustomers
                )

                .totalDrivers(
                        totalDrivers
                )

                .totalVehicles(
                        totalVehicles
                )

                .totalRides(
                        totalRides
                )

                .requestedRides(
                        requestedRides
                )

                .acceptedRides(
                        acceptedRides
                )

                .startedRides(
                        startedRides
                )

                .completedRides(
                        completedRides
                )

                .build();
    }


    // ======================================================
    // ALL RIDES
    // ======================================================

    @Override
    public List<RideResponse> getAllRides() {

        return rideRepository
                .findAll()
                .stream()

                .sorted(
                        Comparator.comparing(
                                Ride::getId,
                                Comparator.reverseOrder()
                        )
                )

                .map(this::mapRideToResponse)

                .collect(Collectors.toList());
    }


    // ======================================================
    // RIDES BY STATUS
    // ======================================================

    @Override
    public List<RideResponse> getRidesByStatus(
            RideStatus status) {

        return rideRepository
                .findByStatus(status)
                .stream()

                .sorted(
                        Comparator.comparing(
                                Ride::getId,
                                Comparator.reverseOrder()
                        )
                )

                .map(this::mapRideToResponse)

                .collect(Collectors.toList());
    }


    // ======================================================
    // CUSTOMERS
    // ======================================================

    @Override
    public List<AdminCustomerResponse> getAllCustomers() {

        return userRepository
                .findAll()
                .stream()

                .filter(user ->
                        "CUSTOMER".equalsIgnoreCase(
                                user.getRole()
                        )
                )

                .sorted(
                        Comparator.comparing(
                                User::getId,
                                Comparator.reverseOrder()
                        )
                )

                .map(user ->
                        AdminCustomerResponse.builder()

                                .id(
                                        user.getId()
                                )

                                .name(
                                        user.getName()
                                )

                                .email(
                                        user.getEmail()
                                )

                                .phone(
                                        user.getPhone()
                                )

                                .role(
                                        user.getRole()
                                )

                                .build()
                )

                .collect(Collectors.toList());
    }


    // ======================================================
    // DRIVERS
    // ======================================================

    @Override
    @Transactional(readOnly = true)
    public List<AdminDriverResponse> getAllDrivers() {

        return driverRepository
                .findAll()
                .stream()

                .sorted(
                        Comparator.comparing(
                                Driver::getId,
                                Comparator.reverseOrder()
                        )
                )

                .map(driver -> {

                    User user =
                            driver.getUser();

                    return AdminDriverResponse.builder()

                            .id(
                                    driver.getId()
                            )

                            .name(
                                    user == null
                                            ? null
                                            : user.getName()
                            )

                            .email(
                                    user == null
                                            ? null
                                            : user.getEmail()
                            )

                            .phone(
                                    user == null
                                            ? null
                                            : user.getPhone()
                            )

                            .licenseNumber(
                                    driver.getLicenseNumber()
                            )

                            .experience(
                                    driver.getExperience()
                            )

                            .rating(
                                    driver.getRating()
                            )

                            .status(
                                    driver.getStatus() == null
                                            ? null
                                            : driver.getStatus()
                                                    .name()
                            )

                            .currentLatitude(
                                    driver.getCurrentLatitude()
                            )

                            .currentLongitude(
                                    driver.getCurrentLongitude()
                            )

                            .build();
                })

                .collect(Collectors.toList());
    }


    // ======================================================
    // VEHICLES
    // ======================================================

    @Override
    @Transactional(readOnly = true)
    public List<AdminVehicleResponse> getAllVehicles() {

        return vehicleRepository
                .findAll()
                .stream()

                .sorted(
                        Comparator.comparing(
                                Vehicle::getId,
                                Comparator.reverseOrder()
                        )
                )

                .map(vehicle -> {

                    Driver driver =
                            vehicle.getDriver();

                    User user =
                            driver == null
                                    ? null
                                    : driver.getUser();

                    return AdminVehicleResponse.builder()

                            .id(
                                    vehicle.getId()
                            )

                            .vehicleNumber(
                                    vehicle.getVehicleNumber()
                            )

                            .vehicleType(
                                    vehicle.getVehicleType() == null
                                            ? null
                                            : vehicle.getVehicleType()
                                                    .name()
                            )

                            .brand(
                                    vehicle.getBrand()
                            )

                            .model(
                                    vehicle.getModel()
                            )

                            .color(
                                    vehicle.getColor()
                            )

                            .seatCapacity(
                                    vehicle.getSeatCapacity()
                            )

                            .status(
                                    vehicle.getStatus() == null
                                            ? null
                                            : vehicle.getStatus()
                                                    .name()
                            )

                            .driverId(
                                    driver == null
                                            ? null
                                            : driver.getId()
                            )

                            .driverName(
                                    user == null
                                            ? null
                                            : user.getName()
                            )

                            .driverEmail(
                                    user == null
                                            ? null
                                            : user.getEmail()
                            )

                            .build();
                })

                .collect(Collectors.toList());
    }


    // ======================================================
    // RIDE MAPPER
    // ======================================================

    private RideResponse mapRideToResponse(
            Ride ride) {

        return RideResponse.builder()

                .id(
                        ride.getId()
                )

                .customer(
                        ride.getCustomer() == null
                                ? null
                                : ride.getCustomer()
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
                        ride.getStatus() == null
                                ? null
                                : ride.getStatus().name()
                )

                .build();
    }
}