package com.uberclone.backend.dto;
import lombok.Data;



@Data
public class LoginResponse {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
}
