package com.soumalya.taxi_backend.controller;

import com.soumalya.taxi_backend.dto.response.AdminCustomerResponse;
import com.soumalya.taxi_backend.dto.response.AdminDashboardResponse;
import com.soumalya.taxi_backend.dto.response.AdminDriverResponse;
import com.soumalya.taxi_backend.dto.response.AdminVehicleResponse;
import com.soumalya.taxi_backend.dto.response.RideResponse;
import com.soumalya.taxi_backend.enums.RideStatus;
import com.soumalya.taxi_backend.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;


    public AdminController(
            AdminService adminService) {

        this.adminService =
                adminService;
    }


    // ======================================================
    // DASHBOARD
    // ======================================================

    @GetMapping("/dashboard")
    public AdminDashboardResponse getDashboard() {

        return adminService.getDashboard();
    }


    // ======================================================
    // RIDES
    // ======================================================

    @GetMapping("/rides")
    public List<RideResponse> getRides(
            @RequestParam(
                    required = false
            )
            String status) {

        if (status == null ||
                status.isBlank() ||
                status.equalsIgnoreCase("ALL")) {

            return adminService.getAllRides();
        }


        RideStatus rideStatus;

        try {

            rideStatus =
                    RideStatus.valueOf(
                            status.toUpperCase()
                    );

        } catch (IllegalArgumentException exception) {

            throw new IllegalArgumentException(
                    "Invalid ride status: " + status
            );
        }


        return adminService.getRidesByStatus(
                rideStatus
        );
    }


    // ======================================================
    // CUSTOMERS
    // ======================================================

    @GetMapping("/customers")
    public List<AdminCustomerResponse> getCustomers() {

        return adminService.getAllCustomers();
    }


    // ======================================================
    // DRIVERS
    // ======================================================

    @GetMapping("/drivers")
    public List<AdminDriverResponse> getDrivers() {

        return adminService.getAllDrivers();
    }


    // ======================================================
    // VEHICLES
    // ======================================================

    @GetMapping("/vehicles")
    public List<AdminVehicleResponse> getVehicles() {

        return adminService.getAllVehicles();
    }
}