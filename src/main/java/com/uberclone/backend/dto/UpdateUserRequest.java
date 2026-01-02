package com.uberclone.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotBlank(message = "name is required")
    String name;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    String email;

    @Size(min = 10,max = 10, message = "Phone must be 10 digits")
    String phoneNo;
}
