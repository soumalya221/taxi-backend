package com.soumalya.taxi_backend.repository;

import com.soumalya.taxi_backend.entity.Driver;
import com.soumalya.taxi_backend.entity.Ride;
import com.soumalya.taxi_backend.entity.RideRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RideRatingRepository
        extends JpaRepository<RideRating, Long> {

    Optional<RideRating> findByRide(Ride ride);

    boolean existsByRide(Ride ride);

    List<RideRating> findByDriver(Driver driver);
}