package com.uberclone.backend.dto;

import lombok.Data;

@Data
public class RideResponse {

    private Long rideId;
    private String status;

    // Ride details
    private String pickupLocation;
    private String dropLocation;

    // Driver details
    private Long driverId;
    private String driverName;
    private String driverPhone;
    private Double fare;

}
