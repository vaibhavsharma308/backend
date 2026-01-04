package com.uberclone.backend.service;

import com.uberclone.backend.entity.Driver;
import com.uberclone.backend.entity.DriverStatus;
import com.uberclone.backend.dto.CreateRideRequest;
import com.uberclone.backend.dto.RideResponse;
import com.uberclone.backend.entity.Ride;
import com.uberclone.backend.entity.RideStatus;
import com.uberclone.backend.repository.DriverRepository;
import com.uberclone.backend.repository.RideRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;

    public RideService(RideRepository rideRepository,
                       DriverRepository driverRepository) {
        this.rideRepository = rideRepository;
        this.driverRepository = driverRepository;
    }

    @Transactional
    public RideResponse createRide(Long userId, CreateRideRequest request) {


        Driver driver = driverRepository
                .findFirstByStatus(DriverStatus.AVAILABLE)
                .orElseThrow(() ->
                        new IllegalStateException("No drivers available"));


        driver.setStatus(DriverStatus.BUSY);
        driverRepository.save(driver);

        com.uberclone.backend.entity.Ride ride = new com.uberclone.backend.entity.Ride();
        ride.setUserId(userId);
        ride.setDriverId(driver.getId());
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDropLocation(request.getDropLocation());
        ride.setStatus(RideStatus.ASSIGNED);

        com.uberclone.backend.entity.Ride savedRide = rideRepository.save(ride);

        RideResponse response = new RideResponse();
        response.setRideId(savedRide.getId());
        response.setDriverId(driver.getId());
        response.setStatus(savedRide.getStatus().name());
        response.setPickupLocation(request.getPickupLocation());
        response.setDropLocation(request.getDropLocation());
        response.setDriverName(driver.getFirstname() + " " + driver.getLastname());
        response.setDriverPhone(driver.getPhoneNo());
        return response;
    }public RideResponse getAssignedRideForDriver(Long driverId) {

        Optional<Ride> optionalRide =
                rideRepository.findFirstByDriverIdAndStatus(
                        driverId,
                        RideStatus.ASSIGNED
                );

        // If no ride assigned, return null
        if (optionalRide.isEmpty()) {
            return null;
        }

        com.uberclone.backend.entity.Ride ride = optionalRide.get();

        RideResponse response = new RideResponse();
        response.setRideId(ride.getId());
        response.setStatus(String.valueOf(ride.getStatus()));
        response.setPickupLocation(ride.getPickupLocation());
        response.setDropLocation(ride.getDropLocation());
        response.setDriverId(ride.getDriverId());

        return response;
    }
    @Transactional
    public RideResponse acceptRide(Long rideId, Long driverId) {

        // 1. Fetch ride (must belong to driver)
        Ride ride = rideRepository.findByIdAndDriverId(rideId, driverId)
                .orElseThrow(() ->
                        new IllegalStateException("Ride not assigned to this driver"));

        // 2. Validate ride state
        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new IllegalStateException("Ride cannot be accepted");
        }

        // 3. Fetch driver
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() ->
                        new IllegalStateException("Driver not found"));

        // 4. Update states
        ride.setStatus(RideStatus.IN_PROGRESS);
        driver.setStatus(DriverStatus.BUSY);

        // 5. Save changes
        rideRepository.save(ride);
        driverRepository.save(driver);

        // 6. Build response
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
    @Transactional
    public void rejectRide(Long rideId, Long driverId) {

        // 1. Fetch ride
        Ride ride = rideRepository.findByIdAndDriverId(rideId, driverId)
                .orElseThrow(() ->
                        new IllegalStateException("Ride not assigned to this driver"));

        // 2. Validate ride state
        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new IllegalStateException("Ride cannot be rejected");
        }

        // 3. Fetch driver
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() ->
                        new IllegalStateException("Driver not found"));

        // 4. Update states
        ride.setStatus(RideStatus.CANCELLED);
        driver.setStatus(DriverStatus.AVAILABLE);

        // 5. Save changes
        rideRepository.save(ride);
        driverRepository.save(driver);
    }

}
