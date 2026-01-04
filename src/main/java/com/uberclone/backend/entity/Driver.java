package com.uberclone.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "drivers")
@Data
@NoArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Personal details
    @Column(nullable = false)
    private String firstname;

    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 10)
    private String phoneNo;

    // Authentication (hashed later)
    @Column(nullable = false)
    private String password;

    // Profile completion (added later)
    private String vehicleNumber;

    // System controlled state
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverStatus status;

    // Driver rating
    private Double rating;
}
