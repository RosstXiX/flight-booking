package io.github.rosstxix.flightbooking.repository;

import io.github.rosstxix.flightbooking.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
}
