package com.uberclone.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DriverRideActionRequest {

    @NotBlank(message = "Action is required")
    private String action; // ACCEPT or REJECT
}
