package io.github.rosstxix.flightbooking.repository;

import io.github.rosstxix.flightbooking.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
}
