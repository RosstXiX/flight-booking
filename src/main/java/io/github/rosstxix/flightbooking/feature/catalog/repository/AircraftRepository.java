package io.github.rosstxix.flightbooking.feature.catalog.repository;

import io.github.rosstxix.flightbooking.feature.catalog.domain.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
}
