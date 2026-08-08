package com.soumalya.taxi_backend.controller;

import com.soumalya.taxi_backend.dto.request.DriverRegisterRequest;
import com.soumalya.taxi_backend.dto.response.DriverResponse;
import com.soumalya.taxi_backend.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/register")
    public DriverResponse registerDriver(
            Authentication authentication,
            @Valid @RequestBody DriverRegisterRequest request) {

        return driverService.registerDriver(
                authentication.getName(),
                request
        );
    }
}