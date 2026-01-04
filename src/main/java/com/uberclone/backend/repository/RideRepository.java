package com.uberclone.backend.repository;

import com.uberclone.backend.entity.Ride;
import com.uberclone.backend.entity.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;



public interface RideRepository extends JpaRepository<Ride, Long> {
    Optional<Ride> findFirstByDriverIdAndStatus(Long driverId, RideStatus status);
    Optional<Ride> findByIdAndDriverId(Long id, Long driverId);

}
