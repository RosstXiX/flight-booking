package io.github.rosstxix.flightbooking.feature.booking.repository;

import io.github.rosstxix.flightbooking.feature.booking.domain.Booking;
import io.github.rosstxix.flightbooking.feature.booking.domain.BookingStatus;
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

}
