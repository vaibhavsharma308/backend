package com.uberclone.backend.repository;

import com.uberclone.backend.entity.Driver;
import com.uberclone.backend.entity.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByEmail(String email);
    Optional<Driver> findFirstByStatus(DriverStatus status);

}