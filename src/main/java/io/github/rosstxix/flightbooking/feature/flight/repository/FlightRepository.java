package io.github.rosstxix.flightbooking.feature.flight.repository;

import io.github.rosstxix.flightbooking.feature.booking.domain.BookingStatus;
import io.github.rosstxix.flightbooking.feature.flight.domain.Flight;
import io.github.rosstxix.flightbooking.feature.flight.domain.FlightStatus;
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
                    ac.totalSeats AS totalSeats,
                    ac.totalSeats - COUNT(b.id) AS availableSeats
                FROM Flight f
                    JOIN f.departureAirport da
                    JOIN f.arrivalAirport aa
                    JOIN f.aircraft ac
                    LEFT JOIN Booking b
                        ON b.flight = f
                        AND b.status = :bookingStatus
                WHERE da.code = :from
                    AND aa.code = :to
                    AND f.departureUtc >= :startUtc
                    AND f.departureUtc < :endUtc
                    AND f.status = :flightStatus
                GROUP BY
                        f.id,
                        f.flightNumber,
                        f.price,
                        f.status,
                        da.code,
                        da.city,
                        da.timeZone,
                        f.departureUtc,
                        aa.code,
                        aa.city,
                        aa.timeZone,
                        f.arrivalUtc,
                        ac.model,
                        ac.totalSeats
            """)
    Page<FlightProjection> searchFlights(
            @Param("from") String fromCode,
            @Param("to") String toCode,
            @Param("startUtc") Instant startUtc,
            @Param("endUtc") Instant endUtc,
            @Param("flightStatus") FlightStatus flightStatus,
            @Param("bookingStatus") BookingStatus bookingStatus,
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
                    ac.totalSeats AS totalSeats,
                    ac.totalSeats - COUNT(b.id) AS availableSeats
                FROM Flight f
                    JOIN f.departureAirport da
                    JOIN f.arrivalAirport aa
                    JOIN f.aircraft ac
                    LEFT JOIN Booking b
                        ON b.flight = f
                        AND b.status = :bookingStatus
                WHERE f.id = :id
                GROUP BY
                        f.id,
                        f.flightNumber,
                        f.price,
                        f.status,
                        da.code,
                        da.city,
                        da.timeZone,
                        f.departureUtc,
                        aa.code,
                        aa.city,
                        aa.timeZone,
                        f.arrivalUtc,
                        ac.model,
                        ac.totalSeats
            """)
    Optional<FlightProjection> findProjectionById(
            @Param("id") Long id,
            @Param("bookingStatus") BookingStatus bookingStatus
    );
}
