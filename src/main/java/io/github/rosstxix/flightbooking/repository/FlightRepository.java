package io.github.rosstxix.flightbooking.repository;

import io.github.rosstxix.flightbooking.domain.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f " +
           "JOIN FETCH f.departureAirport " +
           "JOIN FETCH f.arrivalAirport " +
           "JOIN FETCH f.aircraft " +
           "WHERE f.departureAirport.code = :from AND " +
           "f.arrivalAirport.code = :to AND " +
           "f.departureUtc >= :startUtc AND " +
           "f.departureUtc < :endUtc AND " +
           "f.status = io.github.rosstxix.flightbooking.domain.entity.FlightStatus.SCHEDULED"
    )
    Page<Flight> searchFlights(
            @Param("from") String fromCode,
            @Param("to") String toCode,
            @Param("startUtc") Instant startUtc,
            @Param("endUtc") Instant endUtc,
            Pageable pageable
    );

    @Override
    @Query("SELECT f FROM Flight f " +
          "JOIN FETCH f.departureAirport " +
          "JOIN FETCH f.arrivalAirport " +
          "JOIN FETCH f.aircraft " +
          "WHERE f.id = :id"
    )
    Optional<Flight> findById(@Param("id") Long id);
}
