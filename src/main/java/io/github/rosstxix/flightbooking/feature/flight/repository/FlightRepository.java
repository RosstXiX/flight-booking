package io.github.rosstxix.flightbooking.feature.flight.repository;

import io.github.rosstxix.flightbooking.feature.flight.domain.Flight;
import io.github.rosstxix.flightbooking.feature.flight.dto.projection.FlightProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("""
                SELECT
                    f.id AS id,
                    f.flightNumber AS flightNumber,
                    f.price AS price,
                    f.status AS status,
            
                    da.code AS departureAirportCode,
                    da.city AS departureAirportCity,
                    da.timeZone AS departureAirportTimeZone,
                    f.departureUtc AS departureUtc,
            
                    aa.code AS arrivalAirportCode,
                    aa.city AS arrivalAirportCity,
                    aa.timeZone AS arrivalAirportTimeZone,
                    f.arrivalUtc AS arrivalUtc,
            
                    ac.model AS aircraftModel,
                    ac.totalSeats AS totalSeats
                FROM Flight f
                    JOIN f.departureAirport da
                    JOIN f.arrivalAirport aa
                    JOIN f.aircraft ac
                WHERE da.code = :from
                    AND aa.code = :to
                    AND f.departureUtc >= :startUtc
                    AND f.departureUtc < :endUtc
                    AND f.status = :#{T(io.github.rosstxix.flightbooking.feature.flight.domain.FlightStatus).SCHEDULED}
            """)
    Page<FlightProjection> searchFlights(
            @Param("from") String fromCode,
            @Param("to") String toCode,
            @Param("startUtc") Instant startUtc,
            @Param("endUtc") Instant endUtc,
            Pageable pageable
    );

    @Query("""
                SELECT
                    f.id AS id,
                    f.flightNumber AS flightNumber,
                    f.price AS price,
                    f.status AS status,
            
                    da.code AS departureAirportCode,
                    da.city AS departureAirportCity,
                    da.timeZone AS departureAirportTimeZone,
                    f.departureUtc AS departureUtc,
            
                    aa.code AS arrivalAirportCode,
                    aa.city AS arrivalAirportCity,
                    aa.timeZone AS arrivalAirportTimeZone,
                    f.arrivalUtc AS arrivalUtc,
            
                    ac.model AS aircraftModel,
                    ac.totalSeats AS totalSeats
                FROM Flight f
                    JOIN f.departureAirport da
                    JOIN f.arrivalAirport aa
                    JOIN f.aircraft ac
                WHERE f.id = :id
            """)
    Optional<FlightProjection> findProjectionById(@Param("id") Long id);
}
