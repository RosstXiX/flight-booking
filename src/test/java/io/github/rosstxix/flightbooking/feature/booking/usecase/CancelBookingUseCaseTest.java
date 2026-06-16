package io.github.rosstxix.flightbooking.feature.booking.usecase;

import io.github.rosstxix.flightbooking.feature.booking.domain.Booking;
import io.github.rosstxix.flightbooking.feature.booking.payment.domain.Payment;
import io.github.rosstxix.flightbooking.feature.booking.repository.BookingRepository;
import io.github.rosstxix.flightbooking.feature.flight.domain.Flight;
import io.github.rosstxix.flightbooking.feature.user.domain.User;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.InvalidBookingStateApiException;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.InvalidPaymentStateApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CancelBookingUseCaseTest {

    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private CancelBookingUseCase cancelBookingUseCase;

    // -------------------
    // execute
    // -------------------

    @Test
    void execute_shouldCancelBookingAndRefundPayment_whenHappyPath() {
        // Arrange
        Long userId = 1L;
        Long bookingId = 1L;
        Booking booking = mock(Booking.class);
        User user = mock(User.class);
        Payment payment = mock(Payment.class);
        Flight flight = mock(Flight.class);

        when(bookingRepository.findByIdWithLock(bookingId)).thenReturn(Optional.of(booking));
        when(booking.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(userId);
        when(booking.getPayment()).thenReturn(payment);
        when(booking.getFlight()).thenReturn(flight);

        // Act
        cancelBookingUseCase.execute(userId, bookingId);

        // Assert
        verify(booking).cancelBooking();
        verify(payment).refund();
        verify(flight).incrementAvailableSeats();
    }

    @Test
    void execute_shouldThrowEntityNotFoundApiException_whenBookingDoesNotExist() {
        // Arrange
        Long bookingId = 1L;

        when(bookingRepository.findByIdWithLock(bookingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> cancelBookingUseCase.execute(null, bookingId))
                .isInstanceOf(EntityNotFoundApiException.class);

    }

    @Test
    void execute_shouldThrowEntityNotFoundApiException_whenBookingNotOwnedByUser() {
        // Arrange
        Long userId = 1L;
        Long bookingId = 1L;
        User user = mock(User.class);
        Booking booking = mock(Booking.class);

        when(bookingRepository.findByIdWithLock(bookingId)).thenReturn(Optional.of(booking));
        when(booking.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(2L);

        // Act & Assert
        assertThatThrownBy(() -> cancelBookingUseCase.execute(userId, bookingId))
                .isInstanceOf(EntityNotFoundApiException.class)
                .hasMessageContaining(String.valueOf(userId));
    }

    @Test
    void execute_shouldThrowInvalidPaymentStateApiException_whenPaymentRefunded() {
        // Arrange
        Long userId = 1L;
        Long bookingId = 1L;
        Booking booking = mock(Booking.class);
        Payment payment = mock(Payment.class);
        User user = mock(User.class);

        when(bookingRepository.findByIdWithLock(bookingId)).thenReturn(Optional.of(booking));
        when(booking.getPayment()).thenReturn(payment);
        when(booking.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(userId);
        doThrow(new InvalidPaymentStateApiException("stub: payment already refunded"))
                .when(payment).refund();

        // Act & Assert
        assertThatThrownBy(() -> cancelBookingUseCase.execute(userId, bookingId))
                .isInstanceOf(InvalidPaymentStateApiException.class);
    }

    @Test
    void execute_shouldThrowInvalidBookingStateApiException_whenBookingAlreadyCancelled() {
        // Arrange
        Long userId = 1L;
        Long bookingId = 1L;
        Booking booking = mock(Booking.class);
        User user = mock(User.class);

        when(bookingRepository.findByIdWithLock(bookingId)).thenReturn(Optional.of(booking));
        when(booking.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(userId);
        doThrow(new InvalidBookingStateApiException("stub: booking already cancelled"))
                .when(booking).cancelBooking();

        // Act & Assert
        assertThatThrownBy(() -> cancelBookingUseCase.execute(userId, bookingId))
                .isInstanceOf(InvalidBookingStateApiException.class);
    }
}
