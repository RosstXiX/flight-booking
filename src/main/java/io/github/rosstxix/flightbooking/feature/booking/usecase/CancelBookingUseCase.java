package io.github.rosstxix.flightbooking.feature.booking.usecase;

import io.github.rosstxix.flightbooking.feature.booking.domain.Booking;
import io.github.rosstxix.flightbooking.feature.booking.repository.BookingRepository;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CancelBookingUseCase {

    private final BookingRepository bookingRepository;

    public CancelBookingUseCase(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public void execute(
            Long userId,
            Long bookingId
    ) {
        Booking booking = bookingRepository.findByIdWithLock(bookingId).orElseThrow(
                () -> new EntityNotFoundApiException("Booking with id %d not found".formatted(bookingId))
        );
        if(!booking.getUser().getId().equals(userId)) {
            throw new EntityNotFoundApiException("Booking with id %d not found".formatted(bookingId));
        }

        booking.cancelBooking();
        booking.getPayment().refund();
        booking.getFlight().incrementAvailableSeats();

    }
}
