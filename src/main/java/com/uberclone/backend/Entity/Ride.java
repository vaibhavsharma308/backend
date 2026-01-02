package com.uberclone.backend.entity;

import com.uberclone.backend.enums.RideStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rides")
@Data
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Long driverId;

    private String pickupLocation;
    private String dropLocation;

    @Enumerated(EnumType.STRING)
    private RideStatus status;
}
