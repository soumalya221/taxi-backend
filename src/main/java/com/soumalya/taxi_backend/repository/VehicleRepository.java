package com.soumalya.taxi_backend.repository;

import com.soumalya.taxi_backend.entity.Driver;
import com.soumalya.taxi_backend.entity.Vehicle;
import com.soumalya.taxi_backend.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("SELECT v.id FROM Vehicle v WHERE v.vehicleNumber = :vehicleNumber")
    boolean existsByVehicleNumber(String vehicleNumber);

    boolean existsByDriver(Driver driver);

    Optional<Vehicle> findByDriver(Driver driver);

    Optional<Vehicle> findByDriverAndVehicleType(
            Driver driver,
            VehicleType vehicleType
    );

}
