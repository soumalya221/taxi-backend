package com.soumalya.taxi_backend.repository;

import com.soumalya.taxi_backend.entity.Driver;
import com.soumalya.taxi_backend.entity.Ride;
import com.soumalya.taxi_backend.entity.User;
import com.soumalya.taxi_backend.enums.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findByCustomer(User customer);

    List<Ride> findByDriver(Driver driver);

    List<Ride> findByStatus(RideStatus status);

    Optional<Ride> findById(Long id);

}
