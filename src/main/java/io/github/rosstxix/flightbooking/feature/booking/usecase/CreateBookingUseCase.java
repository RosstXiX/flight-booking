package io.github.rosstxix.flightbooking.feature.booking.usecase;

import io.github.rosstxix.flightbooking.feature.booking.domain.Booking;
import io.github.rosstxix.flightbooking.feature.booking.payment.service.PaymentService;
import io.github.rosstxix.flightbooking.feature.booking.repository.BookingRepository;
import io.github.rosstxix.flightbooking.feature.flight.domain.Flight;
import io.github.rosstxix.flightbooking.feature.flight.repository.FlightRepository;
import io.github.rosstxix.flightbooking.feature.user.repository.UserRepository;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.SeatAlreadyBookedApiException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateBookingUseCase {

    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PaymentService paymentService;

    public CreateBookingUseCase(
            FlightRepository flightRepository,
            UserRepository userRepository,
            BookingRepository bookingRepository,
            PaymentService paymentService
    ) {
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.paymentService = paymentService;
    }

    @Transactional
    public void execute(
            Long userId,
            Long flightId,
            String seatNumber
    ) {

        Flight flight = flightRepository.findByIdWithLock(flightId).orElseThrow(
                () -> new EntityNotFoundApiException("flight with id %d not found".formatted(flightId))
        );
        flight.decrementAvailableSeats();

        flight.getAircraft().validateSeat(seatNumber);

        Booking booking = new Booking(
                userRepository.findById(userId).orElseThrow(
                        () -> new EntityNotFoundApiException("user with id %d not found".formatted(userId))
                ),
                flight,
                seatNumber
        );

        try {
            bookingRepository.saveAndFlush(booking);
        } catch (DataIntegrityViolationException e) {
            throw new SeatAlreadyBookedApiException("Seat with number %s is already booked".formatted(seatNumber));
        }

        paymentService.createPayment(booking);
        booking.confirmBooking();
    }
}
