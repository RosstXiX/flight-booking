package io.github.rosstxix.flightbooking.feature.booking.usecase;

import io.github.rosstxix.flightbooking.feature.booking.domain.BookingStatus;
import io.github.rosstxix.flightbooking.feature.booking.dto.projection.BookingDetailsProjection;
import io.github.rosstxix.flightbooking.feature.booking.dto.response.BookingDetailsResponse;
import io.github.rosstxix.flightbooking.feature.booking.mapper.BookingMapper;
import io.github.rosstxix.flightbooking.feature.booking.payment.domain.PaymentStatus;
import io.github.rosstxix.flightbooking.feature.booking.repository.BookingRepository;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetBookingDetailsUseCaseTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingMapper bookingMapper;
    @InjectMocks
    private GetBookingDetailsUseCase getBookingDetailsUseCase;

    @Test
    void execute_shouldReturnBookingDetails_whenHappyPath() {
        // Arrange
        Long userId = 1L;
        Long bookingId = 1L;

        BookingDetailsProjection projection = createProjection(bookingId);
        BookingDetailsResponse expectedResponse = createExpectedResponse(bookingId);

        when(bookingRepository.existsByIdAndUserId(bookingId, userId)).thenReturn(true);
        when(bookingRepository.findDetailsById(bookingId)).thenReturn(Optional.of(projection));
        when(bookingMapper.toDetailsResponse(projection)).thenReturn(expectedResponse);

        // Act
        BookingDetailsResponse response = getBookingDetailsUseCase.execute(userId, bookingId);

        // Assert
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void execute_shouldThrowEntityNotFoundApiException_whenBookingNotFound() {
        // Arrange
        Long userId = 1L;
        Long bookingId = 1L;

        when(bookingRepository.existsByIdAndUserId(bookingId, userId)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> getBookingDetailsUseCase.execute(userId, bookingId))
                .isInstanceOf(EntityNotFoundApiException.class)
                .hasMessageContaining(String.valueOf(bookingId));
    }

    @Test
    void execute_shouldThrowEntityNotFoundApiException_whenBookingOwnedByOtherUser() {
        // Arrange
        Long userId = 1L;
        Long bookingId = 1L;

        when(bookingRepository.existsByIdAndUserId(bookingId, userId)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> getBookingDetailsUseCase.execute(userId, bookingId))
                .isInstanceOf(EntityNotFoundApiException.class)
                .hasMessageContaining(String.valueOf(bookingId));
    }

    private BookingDetailsProjection createProjection(Long bookingId) {
        return new BookingDetailsProjection() {
            @Override
            public Long getId() { return bookingId; }

            @Override
            public BookingStatus getStatus() { return BookingStatus.CONFIRMED; }

            @Override
            public String getSeatNumber() { return "17B"; }

            @Override
            public Instant getCreatedAt() { return Instant.parse("2026-06-16T10:30:00Z"); }

            @Override
            public Long getFlightId() { return 5L; }

            @Override
            public String getFlightNumber() { return "A1524"; }

            @Override
            public Instant getDepartureUtc() { return Instant.parse("2026-07-01T14:00:00Z"); }

            @Override
            public Instant getArrivalUtc() { return Instant.parse("2026-07-02T02:00:00Z"); }

            @Override
            public String getAircraftModel() { return "Boeing 777"; }

            @Override
            public String getDepartureAirportCode() { return "JFK"; }

            @Override
            public String getDepartureAirportCity() { return "New York"; }

            @Override
            public String getDepartureAirportCountry() { return "USA"; }

            @Override
            public String getArrivalAirportCode() { return "LHR"; }

            @Override
            public String getArrivalAirportCity() { return "London"; }

            @Override
            public String getArrivalAirportCountry() { return "UK"; }

            @Override
            public BigDecimal getPaymentAmount() { return new BigDecimal("450.00"); }

            @Override
            public String getPaymentCurrency() { return "USD"; }

            @Override
            public PaymentStatus getPaymentStatus() { return PaymentStatus.SUCCESS; }
        };
    }

    private BookingDetailsResponse createExpectedResponse(Long bookingId) {
        return new BookingDetailsResponse(
                bookingId,
                "CONFIRMED",
                "17B",
                Instant.parse("2026-06-16T10:30:00Z"),
                new BookingDetailsResponse.FlightInfo(
                        5L,
                        "A1524",
                        new BookingDetailsResponse.AirportInfo("JFK", "New York", "USA"),
                        new BookingDetailsResponse.AirportInfo("LHR", "London", "UK"),
                        Instant.parse("2026-07-01T14:00:00Z"),
                        Instant.parse("2026-07-02T02:00:00Z"),
                        "Boeing 777"
                ),
                new BookingDetailsResponse.PaymentInfo(
                        new BigDecimal("450.00"),
                        "USD",
                        "SUCCESS"
                )
        );
    }
}
