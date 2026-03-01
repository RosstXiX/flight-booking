package io.github.rosstxix.flightbooking.feature.catalog.repository;

import io.github.rosstxix.flightbooking.feature.catalog.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    Optional<Airport> findByCode(String code);
}
