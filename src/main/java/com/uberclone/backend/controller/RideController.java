package com.uberclone.backend.controller;

import com.uberclone.backend.dto.CreateRideRequest;
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
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ride);
    }

    @PostMapping("/{rideId}/accept")
    public RideResponse acceptRide(
            @PathVariable Long rideId,
            @RequestParam Long driverId) {

        return rideService.acceptRide(rideId, driverId);
    }

    @PostMapping("/{rideId}/reject")
    public ResponseEntity<Void> rejectRide(
            @PathVariable Long rideId,
            @RequestParam Long driverId) {

        rideService.rejectRide(rideId, driverId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{rideId}/start")
    public RideResponse startRide(
            @PathVariable Long rideId,
            @RequestParam Long driverId) {

        return rideService.startRide(rideId, driverId);
    }

    @PostMapping("/{rideId}/end")
    public RideResponse endRide(
            @PathVariable Long rideId,
            @RequestParam Long driverId) {

        return rideService.endRide(rideId, driverId);
    }

    @GetMapping("/{rideId}")
    public RideResponse getRideById(@PathVariable Long rideId) {
        return rideService.getRideById(rideId);
    }
}
