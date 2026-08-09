package com.soumalya.taxi_backend.entity;

import com.soumalya.taxi_backend.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "ride_ratings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_ride_rating_ride",
                        columnNames = "ride_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideRating extends BaseEntity {

    @OneToOne(optional = false)
    @JoinColumn(
            name = "ride_id",
            nullable = false,
            unique = true
    )
    private Ride ride;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 500)
    private String comment;
}