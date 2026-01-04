package com.uberclone.backend.controller;

import com.uberclone.backend.dto.*;
import com.uberclone.backend.service.DriverService;
import com.uberclone.backend.service.RideService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;
    private final RideService rideService;
    public DriverController(DriverService driverService, RideService rideService) {
        this.driverService = driverService;
        this.rideService = rideService;
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
    @PatchMapping("/me/availability")
    public DriverProfileResponse updateAvailability(
            @RequestParam Long driverId,
            @Valid @RequestBody UpdateDriverAvailabilityRequest request) {
        return driverService.updateAvailability(driverId, request);
    }
    @GetMapping("/me/assigned-ride")
    public ResponseEntity<?> getAssignedRide(
            @RequestParam Long driverId) {

        RideResponse ride =
                rideService.getAssignedRideForDriver(driverId);

        if (ride == null) {
            return ResponseEntity.noContent().build(); // 204
        }

        return ResponseEntity.ok(ride);
    }
    @PostMapping("/me/rides/{rideId}/accept")
    public RideResponse acceptRide(
            @PathVariable Long rideId,
            @RequestParam Long driverId) {

        return rideService.acceptRide(rideId, driverId);
    }
    @PostMapping("/me/rides/{rideId}/reject")
    public ResponseEntity<Void> rejectRide(
            @PathVariable Long rideId,
            @RequestParam Long driverId) {

        rideService.rejectRide(rideId, driverId);
        return ResponseEntity.noContent().build();
    }


}