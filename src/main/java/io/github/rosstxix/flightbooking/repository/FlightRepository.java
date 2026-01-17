package io.github.rosstxix.flightbooking.repository;

import io.github.rosstxix.flightbooking.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f WHERE " +
           "f.departureAirport.code = :from AND " +
           "f.arrivalAirport.code = :to AND " +
           "f.departureUtc >= :startUtc AND " +
           "f.departureUtc < :endUtc AND " +
           "f.status = 'SCHEDULED'"
    )
    List<Flight> searchFlights(
            @Param("from") String fromCode,
            @Param("to") String toCode,
            @Param("startUtc") Instant startUtc,
            @Param("endUtc") Instant endUtc
    );
}
