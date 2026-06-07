package io.github.rosstxix.flightbooking.feature.catalog.aircraft.repository;

import io.github.rosstxix.flightbooking.feature.catalog.aircraft.domain.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
}
