package com.soumalya.taxi_backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardResponse {

    private long totalCustomers;

    private long totalDrivers;

    private long totalVehicles;

    private long totalRides;

    private long requestedRides;

    private long acceptedRides;

    private long startedRides;

    private long completedRides;
}