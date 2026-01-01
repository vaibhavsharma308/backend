package com.uberclone.backend.dto;

import lombok.Data;

@Data
public class CreateUserRequest {
    String name;
    String email;
    String phoneNo;
}
