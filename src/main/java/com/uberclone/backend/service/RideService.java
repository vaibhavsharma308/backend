package com.uberclone.backend.service;

import com.uberclone.backend.dto.CreateRideRequest;
import com.uberclone.backend.dto.RideResponse;
import com.uberclone.backend.entity.Driver;
import com.uberclone.backend.entity.DriverStatus;
import com.uberclone.backend.entity.Ride;
import com.uberclone.backend.entity.RideStatus;
import com.uberclone.backend.repository.DriverRepository;
import com.uberclone.backend.repository.RideRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RideService {

    private static final double BASE_FARE = 50.0;
    private static final double PER_MINUTE_RATE = 5.0;

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;

    public RideService(RideRepository rideRepository,
                       DriverRepository driverRepository) {
        this.rideRepository = rideRepository;
        this.driverRepository = driverRepository;
    }

    // =========================
    // Rider creates a ride
    // =========================
    @Transactional
    public RideResponse createRide(Long userId, CreateRideRequest request) {

        Driver driver = driverRepository
                .findFirstByStatus(DriverStatus.AVAILABLE)
                .orElseThrow(() ->
                        new IllegalStateException("No drivers available"));

        driver.setStatus(DriverStatus.BUSY);
        driverRepository.save(driver);

        Ride ride = new Ride();
        ride.setUserId(userId);
        ride.setDriverId(driver.getId());
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDropLocation(request.getDropLocation());
        ride.setStatus(RideStatus.ASSIGNED);

        Ride savedRide = rideRepository.save(ride);

        RideResponse response = new RideResponse();
        response.setRideId(savedRide.getId());
        response.setDriverId(driver.getId());
        response.setStatus(savedRide.getStatus().name());
        response.setPickupLocation(savedRide.getPickupLocation());
        response.setDropLocation(savedRide.getDropLocation());
        response.setDriverName(driver.getFirstname() + " " + driver.getLastname());
        response.setDriverPhone(driver.getPhoneNo());

        return response;
    }

    // =========================
    // Driver polls for ride
    // =========================
    public RideResponse getAssignedRideForDriver(Long driverId) {

        Optional<Ride> optionalRide =
                rideRepository.findFirstByDriverIdAndStatus(
                        driverId,
                        RideStatus.ASSIGNED
                );

        if (optionalRide.isEmpty()) {
            return null;
        }

        Ride ride = optionalRide.get();

        RideResponse response = new RideResponse();
        response.setRideId(ride.getId());
        response.setStatus(ride.getStatus().name());
        response.setPickupLocation(ride.getPickupLocation());
        response.setDropLocation(ride.getDropLocation());
        response.setDriverId(ride.getDriverId());

        return response;
    }

    // =========================
    // Driver accepts ride
    // =========================
    @Transactional
    public RideResponse acceptRide(Long rideId, Long driverId) {

        Ride ride = rideRepository.findByIdAndDriverId(rideId, driverId)
                .orElseThrow(() ->
                        new IllegalStateException("Ride not assigned to this driver"));

        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new IllegalStateException("Ride cannot be accepted");
        }

        ride.setStatus(RideStatus.IN_PROGRESS);
        rideRepository.save(ride);

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() ->
                        new IllegalStateException("Driver not found"));

        RideResponse response = new RideResponse();
        response.setRideId(ride.getId());
        response.setStatus(ride.getStatus().name());
        response.setPickupLocation(ride.getPickupLocation());
        response.setDropLocation(ride.getDropLocation());
        response.setDriverId(driver.getId());
        response.setDriverName(driver.getFirstname() + " " + driver.getLastname());
        response.setDriverPhone(driver.getPhoneNo());

        return response;
    }

    // =========================
    // Driver rejects ride
    // =========================
    @Transactional
    public void rejectRide(Long rideId, Long driverId) {

        Ride ride = rideRepository.findByIdAndDriverId(rideId, driverId)
                .orElseThrow(() ->
                        new IllegalStateException("Ride not assigned to this driver"));

        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new IllegalStateException("Ride cannot be rejected");
        }

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() ->
                        new IllegalStateException("Driver not found"));

        ride.setStatus(RideStatus.CANCELLED);
        driver.setStatus(DriverStatus.AVAILABLE);

        rideRepository.save(ride);
        driverRepository.save(driver);
    }

    // =========================
    // Driver starts ride
    // =========================
    @Transactional
    public RideResponse startRide(Long rideId, Long driverId) {

        Ride ride = rideRepository.findByIdAndDriverId(rideId, driverId)
                .orElseThrow(() ->
                        new IllegalStateException("Ride not assigned to this driver"));

        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new IllegalStateException("Ride cannot be started");
        }

        if (ride.getStartedAt() != null) {
            throw new IllegalStateException("Ride already started");
        }

        ride.setStartedAt(LocalDateTime.now());
        rideRepository.save(ride);

        RideResponse response = new RideResponse();
        response.setRideId(ride.getId());
        response.setStatus(ride.getStatus().name());
        response.setPickupLocation(ride.getPickupLocation());
        response.setDropLocation(ride.getDropLocation());
        response.setDriverId(driverId);

        return response;
    }

    // =========================
    // Driver ends ride + fare
    // =========================
    @Transactional
    public RideResponse endRide(Long rideId, Long driverId) {

        Ride ride = rideRepository.findByIdAndDriverId(rideId, driverId)
                .orElseThrow(() ->
                        new IllegalStateException("Ride not assigned to this driver"));

        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new IllegalStateException("Ride cannot be ended");
        }

        if (ride.getStartedAt() == null) {
            throw new IllegalStateException("Ride has not been started yet");
        }

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalStateException("Driver not found"));

        ride.setEndedAt(LocalDateTime.now());

        long minutes = Duration.between(
                ride.getStartedAt(),
                ride.getEndedAt()
        ).toMinutes();

        if (minutes < 1) {
            minutes = 1;
        }

        double fare = BASE_FARE + (minutes * PER_MINUTE_RATE);

        ride.setFare(fare);
        ride.setStatus(RideStatus.COMPLETED);
        driver.setStatus(DriverStatus.AVAILABLE);

        rideRepository.save(ride);
        driverRepository.save(driver);

        RideResponse response = new RideResponse();
        response.setRideId(ride.getId());
        response.setStatus(ride.getStatus().name());
        response.setPickupLocation(ride.getPickupLocation());
        response.setDropLocation(ride.getDropLocation());
        response.setDriverId(driverId);
        response.setFare(fare);

        return response;
    }

    // =========================
    // Rider checks ride
    // =========================
    public RideResponse getRideById(Long rideId) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new IllegalStateException("Ride not found"));

        RideResponse response = new RideResponse();
        response.setRideId(ride.getId());
        response.setStatus(ride.getStatus().name());
        response.setPickupLocation(ride.getPickupLocation());
        response.setDropLocation(ride.getDropLocation());
        response.setDriverId(ride.getDriverId());
        response.setFare(ride.getFare());

        return response;
    }
}
