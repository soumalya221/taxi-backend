package com.soumalya.taxi_backend.controller;

import com.soumalya.taxi_backend.dto.request.VehicleRegisterRequest;
import com.soumalya.taxi_backend.dto.response.VehicleResponse;
import com.soumalya.taxi_backend.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(
            VehicleService vehicleService) {

        this.vehicleService = vehicleService;
    }

    @PostMapping("/register")
    public VehicleResponse registerVehicle(

            Authentication authentication,

            @Valid
            @RequestBody VehicleRegisterRequest request) {

        return vehicleService.registerVehicle(

                authentication.getName(),
                request
        );
    }
}