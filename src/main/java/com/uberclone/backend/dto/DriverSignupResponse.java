package com.uberclone.backend.dto;

import lombok.Data;

@Data
public class DriverSignupResponse {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNo;
    private String status;
}