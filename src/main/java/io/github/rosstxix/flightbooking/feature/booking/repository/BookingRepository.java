package io.github.rosstxix.flightbooking.feature.booking.repository;

import io.github.rosstxix.flightbooking.feature.booking.domain.Booking;
import io.github.rosstxix.flightbooking.feature.booking.domain.BookingStatus;
import io.github.rosstxix.flightbooking.feature.booking.dto.projection.BookingDetailsProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByFlightId(Long flightId);

    @Query("""
            SELECT
                  b.seatNumber
            FROM Booking b
            WHERE b.flight.id = :flightId
                 AND b.status <> :exceptStatus
            """)
    Set<String> findOccupiedSeatNumbersByFlightId(
            @Param("flightId") Long flightId,
            @Param("exceptStatus") BookingStatus exceptStatus
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT
                  b
            FROM Booking b
                  JOIN FETCH b.flight f
                  JOIN FETCH b.payment p
            WHERE b.id = :id
            """)
    Optional<Booking> findByIdWithLock(@Param("id") Long id);

    @Query("""
            SELECT
                  b.id AS id,
                  b.status AS status,
                  b.seatNumber AS seatNumber,
                  b.createdAt AS createdAt,
                  f.id AS flightId,
                  f.flightNumber AS flightNumber,
                  f.departureUtc AS departureUtc,
                  f.arrivalUtc AS arrivalUtc,
                  a.model AS aircraftModel,
                  dep.code AS departureAirportCode,
                  dep.city AS departureAirportCity,
                  dep.country AS departureAirportCountry,
                  arr.code AS arrivalAirportCode,
                  arr.city AS arrivalAirportCity,
                  arr.country AS arrivalAirportCountry,
                  p.amount AS paymentAmount,
                  p.currency AS paymentCurrency,
                  p.status AS paymentStatus
            FROM Booking b
                  JOIN b.flight f
                  JOIN f.aircraft a
                  JOIN f.departureAirport dep
                  JOIN f.arrivalAirport arr
                  LEFT JOIN b.payment p
            WHERE b.id = :id
            """)
    Optional<BookingDetailsProjection> findDetailsById(@Param("id") Long id);

    @Query("""
            SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
            FROM Booking b
            WHERE b.id = :id AND b.user.id = :userId
            """)
    boolean existsByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

}
