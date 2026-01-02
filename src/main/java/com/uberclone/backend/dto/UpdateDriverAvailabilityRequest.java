package com.uberclone.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDriverAvailabilityRequest {

    @NotNull(message = "Status is required")
    private String status; // AVAILABLE or OFFLINE
}