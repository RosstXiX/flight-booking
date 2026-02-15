package io.github.rosstxix.flightbooking.feature.booking.repository;

import io.github.rosstxix.flightbooking.feature.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByFlightId(Long flightId);
}
