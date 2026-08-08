package com.soumalya.taxi_backend.service.impl;

import com.soumalya.taxi_backend.dto.request.VehicleRegisterRequest;
import com.soumalya.taxi_backend.dto.response.VehicleResponse;
import com.soumalya.taxi_backend.entity.Driver;
import com.soumalya.taxi_backend.entity.User;
import com.soumalya.taxi_backend.entity.Vehicle;
import com.soumalya.taxi_backend.enums.VehicleStatus;
import com.soumalya.taxi_backend.exception.VehicleAlreadyExistsException;
import com.soumalya.taxi_backend.repository.DriverRepository;
import com.soumalya.taxi_backend.repository.UserRepository;
import com.soumalya.taxi_backend.repository.VehicleRepository;
import com.soumalya.taxi_backend.service.VehicleService;
import org.springframework.stereotype.Service;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final UserRepository userRepository;

    public VehicleServiceImpl(
            VehicleRepository vehicleRepository,
            DriverRepository driverRepository,
            UserRepository userRepository) {

        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.userRepository = userRepository;
    }

    @Override
    public VehicleResponse registerVehicle(
            String email,
            VehicleRegisterRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Driver profile not found"));

        if (vehicleRepository.existsByDriver(driver)) {
            throw new VehicleAlreadyExistsException(
                    "Driver already has a vehicle");
        }

        if (vehicleRepository.existsByVehicleNumber(
                request.getVehicleNumber())) {

            throw new VehicleAlreadyExistsException(
                    "Vehicle number already exists");
        }

        Vehicle vehicle = new Vehicle();

        vehicle.setDriver(driver);
        vehicle.setVehicleNumber(request.getVehicleNumber());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setColor(request.getColor());
        vehicle.setSeatCapacity(request.getSeatCapacity());
        vehicle.setStatus(VehicleStatus.AVAILABLE);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return VehicleResponse.builder()
                .id(savedVehicle.getId())
                .vehicleNumber(savedVehicle.getVehicleNumber())
                .vehicleType(savedVehicle.getVehicleType().name())
                .brand(savedVehicle.getBrand())
                .model(savedVehicle.getModel())
                .color(savedVehicle.getColor())
                .seatCapacity(savedVehicle.getSeatCapacity())
                .status(savedVehicle.getStatus().name())
                .build();
    }
}