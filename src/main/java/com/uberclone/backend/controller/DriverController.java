package com.uberclone.backend.controller;

import com.uberclone.backend.dto.*;
import com.uberclone.backend.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/signup")
    public DriverSignupResponse signup(
            @Valid @RequestBody CreateDriverSignupRequest request) {
        return driverService.signup(request);
    }
    @PostMapping("/login")
    public DriverLoginResponse login(
            @Valid @RequestBody DriverLoginRequest request) {
        return driverService.login(request);
    }
    @PutMapping("/me/profile")
    public DriverProfileResponse completeProfile(
            @RequestParam Long driverId,
            @Valid @RequestBody UpdateDriverProfileRequest request) {
        return driverService.completeProfile(driverId, request);
    }

}