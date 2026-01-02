package com.uberclone.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "name is required")
    private String firstname;
    private String lastname;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    String email;

    @Size(min = 10,max = 10, message = "Phone must be 10 digits")
    String phoneNo;

    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "Password must be at least 6 characters long")
    private String password;

    private String confirmPassword;
}
