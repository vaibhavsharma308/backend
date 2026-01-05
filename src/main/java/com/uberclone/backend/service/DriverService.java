package com.uberclone.backend.service;

import com.uberclone.backend.entity.DriverStatus;
import com.uberclone.backend.dto.*;
import com.uberclone.backend.entity.Driver;
import com.uberclone.backend.exception.ResourceNotFoundException;
import com.uberclone.backend.repository.DriverRepository;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }
    public DriverSignupResponse signup(CreateDriverSignupRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirm password do not match");
        }
        Driver driver = new Driver();
        driver.setFirstname(request.getFirstname());
        driver.setLastname(request.getLastname());
        driver.setEmail(request.getEmail());
        driver.setPhoneNo(request.getPhoneNo());
        driver.setPassword(request.getPassword());
        driver.setStatus(DriverStatus.INCOMPLETE_PROFILE);
        driver.setRating(5.0);
        Driver savedDriver = driverRepository.save(driver);


        DriverSignupResponse response = new DriverSignupResponse();
        response.setId(savedDriver.getId());
        response.setFirstname(savedDriver.getFirstname());
        response.setLastname(savedDriver.getLastname());
        response.setEmail(savedDriver.getEmail());
        response.setPhoneNo(savedDriver.getPhoneNo());
        response.setStatus(savedDriver.getStatus().name());

        return response;
    }

    public DriverLoginResponse login(DriverLoginRequest request) {

        Driver driver = driverRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or password"));


        if (!driver.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        DriverLoginResponse response = new DriverLoginResponse();
        response.setId(driver.getId());
        response.setFirstname(driver.getFirstname());
        response.setLastname(driver.getLastname());
        response.setEmail(driver.getEmail());
        response.setPhoneNo(driver.getPhoneNo());
        response.setStatus(driver.getStatus().name());

        return response;
    }
    public DriverProfileResponse completeProfile(
            Long driverId,
            UpdateDriverProfileRequest request) {

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Driver not found"));

        driver.setVehicleNumber(request.getVehicleNumber());

        // Once profile is complete, driver can be OFFLINE
        driver.setStatus(DriverStatus.OFFLINE);

        Driver saved = driverRepository.save(driver);

        DriverProfileResponse response = new DriverProfileResponse();
        response.setId(saved.getId());
        response.setFirstname(saved.getFirstname());
        response.setLastname(saved.getLastname());
        response.setEmail(saved.getEmail());
        response.setPhoneNo(saved.getPhoneNo());
        response.setVehicleNumber(saved.getVehicleNumber());
        response.setStatus(saved.getStatus().name());

        return response;
    }
    public DriverProfileResponse updateAvailability(
            Long driverId,
            UpdateDriverAvailabilityRequest request) {

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        // Guard: profile must be complete
        if (driver.getStatus() == DriverStatus.INCOMPLETE_PROFILE) {
            throw new IllegalStateException("Complete profile before changing availability");
        }

        DriverStatus newStatus;
        try {
            newStatus = DriverStatus.valueOf(request.getStatus());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid status value");
        }

        // Guard: frontend cannot set BUSY
        if (newStatus == DriverStatus.BUSY) {
            throw new IllegalStateException("Status BUSY is system controlled");
        }

        // Only allow OFFLINE <-> AVAILABLE from frontend
        if (!(newStatus == DriverStatus.OFFLINE || newStatus == DriverStatus.AVAILABLE)) {
            throw new IllegalArgumentException("Invalid availability transition");
        }

        driver.setStatus(newStatus);
        Driver saved = driverRepository.save(driver);

        DriverProfileResponse response = new DriverProfileResponse();
        response.setId(saved.getId());
        response.setFirstname(saved.getFirstname());
        response.setLastname(saved.getLastname());
        response.setEmail(saved.getEmail());
        response.setPhoneNo(saved.getPhoneNo());
        response.setVehicleNumber(saved.getVehicleNumber());
        response.setStatus(saved.getStatus().name());

        return response;
    }

}
