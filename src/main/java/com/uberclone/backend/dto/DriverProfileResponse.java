package com.uberclone.backend.dto;

import lombok.Data;

@Data
public class DriverProfileResponse {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNo;
    private String vehicleNumber;
    private String status;
}