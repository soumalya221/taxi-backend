package com.soumalya.taxi_backend.entity;

import com.soumalya.taxi_backend.entity.base.BaseEntity;
import com.soumalya.taxi_backend.enums.RideStatus;
import com.soumalya.taxi_backend.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rides")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(nullable = false)
    private String pickupLocation;

    @Column(nullable = false)
    private String dropLocation;

    private Double pickupLatitude;

    private Double pickupLongitude;

    private Double dropLatitude;

    private Double dropLongitude;

    private Double distance;

    @Column
    private Double durationMinutes;

    private Double fare;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    private RideStatus status;
}
