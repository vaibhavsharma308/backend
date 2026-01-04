package com.uberclone.backend.controller;

import com.uberclone.backend.dto.CreateRideRequest;
import com.uberclone.backend.dto.DriverRideActionRequest;
import com.uberclone.backend.dto.RideResponse;
import com.uberclone.backend.service.RideService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    public RideResponse createRide(
            @RequestParam Long userId,
            @Valid @RequestBody CreateRideRequest request) {

        return rideService.createRide(userId, request);
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

}
