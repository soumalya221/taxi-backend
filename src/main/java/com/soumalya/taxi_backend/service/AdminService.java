package com.soumalya.taxi_backend.service;

import com.soumalya.taxi_backend.dto.response.AdminCustomerResponse;
import com.soumalya.taxi_backend.dto.response.AdminDashboardResponse;
import com.soumalya.taxi_backend.dto.response.AdminDriverResponse;
import com.soumalya.taxi_backend.dto.response.AdminVehicleResponse;
import com.soumalya.taxi_backend.dto.response.RideResponse;
import com.soumalya.taxi_backend.enums.RideStatus;

import java.util.List;

public interface AdminService {

    // Dashboard
    AdminDashboardResponse getDashboard();


    // Rides
    List<RideResponse> getAllRides();

    List<RideResponse> getRidesByStatus(
            RideStatus status
    );


    // Customers
    List<AdminCustomerResponse> getAllCustomers();


    // Drivers
    List<AdminDriverResponse> getAllDrivers();


    // Vehicles
    List<AdminVehicleResponse> getAllVehicles();
}