package com.soumalya.taxi_backend.entity;

import com.soumalya.taxi_backend.entity.base.BaseEntity;
import com.soumalya.taxi_backend.enums.DriverStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "drivers")
public class Driver extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Column(nullable = false)
    private Integer experience;

    @Column(nullable = false)
    private Double rating = 5.0;

    @Enumerated(EnumType.STRING)
    private DriverStatus status = DriverStatus.OFFLINE;

    private Double currentLatitude;

    private Double currentLongitude;

}
