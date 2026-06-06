package io.github.rosstxix.flightbooking.feature.booking.repository;

import io.github.rosstxix.flightbooking.feature.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByFlightId(Long flightId);

    @Query("""
            SELECT
                  b.seatNumber
            FROM Booking b
            WHERE b.flight.id = :flightId
                 AND b.status <> 'CANCELLED'
            """)
    Set<String> findOccupiedSeatNumbersByFlightId(@Param("flightId") Long flightId);}
