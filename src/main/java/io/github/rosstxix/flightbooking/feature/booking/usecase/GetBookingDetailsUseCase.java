package io.github.rosstxix.flightbooking.feature.booking.usecase;

import io.github.rosstxix.flightbooking.feature.booking.dto.projection.BookingDetailsProjection;
import io.github.rosstxix.flightbooking.feature.booking.dto.response.BookingDetailsResponse;
import io.github.rosstxix.flightbooking.feature.booking.mapper.BookingMapper;
import io.github.rosstxix.flightbooking.feature.booking.repository.BookingRepository;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GetBookingDetailsUseCase {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public GetBookingDetailsUseCase(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Transactional(readOnly = true)
    public BookingDetailsResponse execute(Long userId, Long bookingId) {
        if (!bookingRepository.existsByIdAndUserId(bookingId, userId)) {
            throw new EntityNotFoundApiException("Booking with id %d not found".formatted(bookingId));
        }

        BookingDetailsProjection projection = bookingRepository.findDetailsById(bookingId)
                .orElseThrow(() -> new EntityNotFoundApiException("Booking with id %d not found".formatted(bookingId)));

        return bookingMapper.toDetailsResponse(projection);
    }
}
