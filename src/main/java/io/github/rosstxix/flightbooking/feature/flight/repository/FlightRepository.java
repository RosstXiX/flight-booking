package io.github.rosstxix.flightbooking.feature.flight.repository;

import io.github.rosstxix.flightbooking.feature.flight.domain.Flight;
import io.github.rosstxix.flightbooking.feature.flight.domain.FlightStatus;
import io.github.rosstxix.flightbooking.feature.flight.dto.projection.FlightProjection;
import io.github.rosstxix.flightbooking.feature.flight.dto.projection.SeatMapInfoProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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
                    f.currency AS currency,
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
                    ac.totalSeats AS totalSeats,
                    f.availableSeats AS availableSeats
                FROM Flight f
                    JOIN f.departureAirport da
                    JOIN f.arrivalAirport aa
                    JOIN f.aircraft ac
                WHERE da.code = :from
                    AND aa.code = :to
                    AND f.departureUtc >= :startUtc
                    AND f.departureUtc < :endUtc
                    AND f.status = :status
            """)
    Page<FlightProjection> searchFlights(
            @Param("from") String fromCode,
            @Param("to") String toCode,
            @Param("startUtc") Instant startUtc,
            @Param("endUtc") Instant endUtc,
            @Param("status") FlightStatus status,
            Pageable pageable
    );

    @Query("""
                SELECT
                    f.id AS id,
                    f.flightNumber AS flightNumber,
                    f.price AS price,
                    f.currency AS currency,
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
                    ac.totalSeats AS totalSeats,
                    f.availableSeats AS availableSeats
                FROM Flight f
                    JOIN f.departureAirport da
                    JOIN f.arrivalAirport aa
                    JOIN f.aircraft ac
                WHERE f.id = :id
            """)
    Optional<FlightProjection> findProjectionById(@Param("id") Long id);

    @Query("""
            SELECT
                  ac.model AS aircraftModel,
                  ac.totalSeats AS totalSeats,
                  ac.seatLayout AS seatLayout,
                  ac.rows AS rows,
                  ac.seatPerRow AS seatPerRow,
                  ac.premiumSeatLayout AS premiumSeatLayout,
                  ac.premiumRows AS premiumRows,
                  ac.seatPerPremiumRow AS seatPerPremiumRow
            FROM Flight f
                  JOIN f.aircraft ac
            WHERE f.id = :id
            """)
    Optional<SeatMapInfoProjection> findSeatMapInfoProjection(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT
                      f
                FROM Flight f
                      JOIN FETCH f.aircraft
                WHERE f.id = :id
            """)
    Optional<Flight> findByIdWithLock(@Param("id") Long id);
}
