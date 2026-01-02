package com.uberclone.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateDriverProfileRequest {

    @NotBlank(message = "Vehicle number is required")
    private String vehicleNumber;
}
