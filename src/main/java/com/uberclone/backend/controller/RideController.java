package com.uberclone.backend.controller;

import com.uberclone.backend.dto.CreateRideRequest;
import com.uberclone.backend.dto.RideResponse;
import com.uberclone.backend.service.RideService;
import jakarta.validation.Valid;
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
}
