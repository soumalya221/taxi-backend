package com.soumalya.taxi_backend.service.impl;

import com.soumalya.taxi_backend.dto.request.DriverRegisterRequest;
import com.soumalya.taxi_backend.dto.response.DriverResponse;
import com.soumalya.taxi_backend.entity.Driver;
import com.soumalya.taxi_backend.entity.User;
import com.soumalya.taxi_backend.enums.DriverStatus;
import com.soumalya.taxi_backend.exception.DriverAlreadyExistsException;
import com.soumalya.taxi_backend.exception.LicenseAlreadyExistsException;
import com.soumalya.taxi_backend.repository.DriverRepository;
import com.soumalya.taxi_backend.repository.UserRepository;
import com.soumalya.taxi_backend.service.DriverService;
import org.springframework.stereotype.Service;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;

    public DriverServiceImpl(DriverRepository driverRepository,
                             UserRepository userRepository) {
        this.driverRepository = driverRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DriverResponse registerDriver(String email,
                                         DriverRegisterRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (driverRepository.existsByUser(user)) {
            throw new DriverAlreadyExistsException("Driver profile already exists");
        }

        if (driverRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new LicenseAlreadyExistsException("License number already exists");
        }

        Driver driver = new Driver();

        driver.setUser(user);
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setExperience(request.getExperience());
        driver.setRating(5.0);
        driver.setStatus(DriverStatus.OFFLINE);

        Driver savedDriver = driverRepository.save(driver);

        return DriverResponse.builder()
                .id(savedDriver.getId())
                .name(user.getName())
                .email(user.getEmail())
                .licenseNumber(savedDriver.getLicenseNumber())
                .experience(savedDriver.getExperience())
                .rating(savedDriver.getRating())
                .status(savedDriver.getStatus().name())
                .build();
    }
}