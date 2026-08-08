package com.soumalya.taxi_backend.repository;

import com.soumalya.taxi_backend.entity.Driver;
import com.soumalya.taxi_backend.entity.User;
import com.soumalya.taxi_backend.enums.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByUser(User user);

    boolean existsByUser(User user);

    boolean existsByLicenseNumber(String licenseNumber);

    List<Driver> findByStatus(DriverStatus status);
}
