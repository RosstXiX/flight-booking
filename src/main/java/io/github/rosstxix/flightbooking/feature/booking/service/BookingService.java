package io.github.rosstxix.flightbooking.feature.booking.service;

import io.github.rosstxix.flightbooking.feature.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    public Set<String> getOccupiedSeatNumbers(Long id) {
        return bookingRepository.findOccupiedSeatNumbersByFlightId(id);
    }
}
