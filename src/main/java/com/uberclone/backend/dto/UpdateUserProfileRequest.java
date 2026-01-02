package com.uberclone.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserProfileRequest {

    @NotBlank(message = "First name is required")
    private String firstname;

    private String lastname;

    @Size(min = 10, max = 10, message = "Phone must be 10 digits")
    private String phoneNo;
}