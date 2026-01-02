package com.uberclone.backend.Entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    @Column(nullable = false)
    private String password;
}
