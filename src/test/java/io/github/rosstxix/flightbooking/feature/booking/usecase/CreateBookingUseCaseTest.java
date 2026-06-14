package io.github.rosstxix.flightbooking.feature.booking.usecase;

import io.github.rosstxix.flightbooking.feature.booking.domain.Booking;
import io.github.rosstxix.flightbooking.feature.booking.domain.BookingStatus;
import io.github.rosstxix.flightbooking.feature.booking.payment.service.PaymentService;
import io.github.rosstxix.flightbooking.feature.booking.repository.BookingRepository;
import io.github.rosstxix.flightbooking.feature.catalog.aircraft.domain.Aircraft;
import io.github.rosstxix.flightbooking.feature.flight.domain.Flight;
import io.github.rosstxix.flightbooking.feature.flight.repository.FlightRepository;
import io.github.rosstxix.flightbooking.feature.user.domain.User;
import io.github.rosstxix.flightbooking.feature.user.repository.UserRepository;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.NoSeatsAvailableApiException;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.SeatAlreadyBookedApiException;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.SeatDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateBookingUseCaseTest {

    @Mock
    private FlightRepository flightRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private PaymentService paymentService;
    @InjectMocks
    CreateBookingUseCase createBookingUseCase;

    // -------------------
    // execute
    // -------------------

    @Test
    void execute_shouldCreateBookingAndPayment_whenHappyPath() {
        // Arrange
        Long userId = 1L;
        Long flightId = 1L;
        String seatNumber = "19A";

        Aircraft aircraft = mock(Aircraft.class);
        Flight flight = mock(Flight.class);
        User user = new User(
                null, null, null, null
        );

        when(flightRepository.findByIdWithLock(flightId)).thenReturn(Optional.of(flight));
        when(flight.getAircraft()).thenReturn(aircraft);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        createBookingUseCase.execute(userId, flightId, seatNumber);

        // Assert
        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);

        verify(flight).decrementAvailableSeats();
        verify(bookingRepository).saveAndFlush(bookingCaptor.capture());
        verify(paymentService).createPayment(bookingCaptor.capture());

        Booking forRepo = bookingCaptor.getAllValues().get(0);
        Booking forPayment = bookingCaptor.getAllValues().get(1);

        assertThat(forPayment).isSameAs(forRepo);

        assertThat(forRepo.getSeatNumber()).isEqualTo(seatNumber);
        assertThat(forRepo.getFlight()).isSameAs(flight);
        assertThat(forRepo.getUser()).isSameAs(user);
        assertThat(forRepo.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
    }

    @Test
    void execute_shouldThrowEntityNotFoundApiException_whenFlightNotFound() {
        // Arrange
        Long flightId = 1L;
        when(flightRepository.findByIdWithLock(flightId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> createBookingUseCase.execute(null, flightId, null))
                .isInstanceOf(EntityNotFoundApiException.class)
                .hasMessageContaining(String.valueOf(flightId));
    }

    @Test
    void execute_shouldNotCreateBookingAndPayment_whenInvalidSeat() {
        // Arrange
        Long flightId = 1L;
        String seatNumber = "99X";

        Aircraft aircraft = mock(Aircraft.class);
        Flight flight = new Flight(null, null, null, aircraft, null, null, null, null, null);

        when(flightRepository.findByIdWithLock(flightId)).thenReturn(Optional.of(flight));
        doThrow(new SeatDoesNotExistException("stub: invalid seat"))
                .when(aircraft).validateSeat(seatNumber);

        // Act & Assert
        assertThatThrownBy(() -> createBookingUseCase.execute(null, flightId, seatNumber))
                .isInstanceOf(SeatDoesNotExistException.class);
        verifyNoInteractions(bookingRepository, paymentService);
    }

    @Test
    void execute_shouldNotCreateBookingAndPayment_whenNoAvailableSeats() {
        // Arrange
        Long flightId = 1L;
        String seatNumber = "9A";

        Aircraft aircraft = mock(Aircraft.class);
        Flight flight = mock(Flight.class);

        when(flightRepository.findByIdWithLock(flightId)).thenReturn(Optional.of(flight));
        when(flight.getAircraft()).thenReturn(aircraft);

        doThrow(new NoSeatsAvailableApiException("stub: no available seats"))
                .when(flight).decrementAvailableSeats();

        // Act & Assert
        assertThatThrownBy(() -> createBookingUseCase.execute(null, flightId, seatNumber))
                .isInstanceOf(NoSeatsAvailableApiException.class);
        verifyNoInteractions(bookingRepository, paymentService);
    }

    @Test
    void execute_shouldThrowEntityNotFoundApiException_whenUserNotFound() {
        // Arrange
        Long flightId = 1L;
        Long userId = 1L;
        Flight flight = mock(Flight.class);
        Aircraft aircraft = mock(Aircraft.class);

        when(flightRepository.findByIdWithLock(flightId)).thenReturn(Optional.of(flight));
        when(flight.getAircraft()).thenReturn(aircraft);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> createBookingUseCase.execute(userId, flightId, "1A"))
                .isInstanceOf(EntityNotFoundApiException.class)
                .hasMessageContaining(String.valueOf(userId));

        verifyNoInteractions(bookingRepository, paymentService);
    }

    @Test
    void execute_shouldThrowSeatAlreadyBookedApiException_whenSeatAlreadyBooked() {
        // Arrange
        Long flightId = 1L;
        Long userId = 1L;
        Flight flight = mock(Flight.class);
        Aircraft aircraft = mock(Aircraft.class);

        when(flightRepository.findByIdWithLock(flightId)).thenReturn(Optional.of(flight));
        when(flight.getAircraft()).thenReturn(aircraft);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User(null, null, null, null)));

        doThrow(new DataIntegrityViolationException("stub: duplicate key"))
                .when(bookingRepository).saveAndFlush(any(Booking.class));

        // Act & Assert
        assertThatThrownBy(() -> createBookingUseCase.execute(userId, flightId, "1A"))
                .isInstanceOf(SeatAlreadyBookedApiException.class);

        verifyNoInteractions(paymentService);
    }

}
