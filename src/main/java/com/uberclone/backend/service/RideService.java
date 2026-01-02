package com.uberclone.backend.service;

import com.uberclone.backend.Entity.Driver;
import com.uberclone.backend.Entity.DriverStatus;
import com.uberclone.backend.dto.CreateRideRequest;
import com.uberclone.backend.dto.RideResponse;
import com.uberclone.backend.repository.DriverRepository;
import com.uberclone.backend.repository.RideRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
        ride.setStatus(com.uberclone.backend.enums.RideStatus.ASSIGNED);

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
    }
}
