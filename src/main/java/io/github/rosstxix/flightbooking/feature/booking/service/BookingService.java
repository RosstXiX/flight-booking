package io.github.rosstxix.flightbooking.feature.booking.service;

import io.github.rosstxix.flightbooking.feature.booking.domain.Booking;
import io.github.rosstxix.flightbooking.feature.booking.payment.service.PaymentService;
import io.github.rosstxix.flightbooking.feature.booking.repository.BookingRepository;
import io.github.rosstxix.flightbooking.feature.flight.domain.Flight;
import io.github.rosstxix.flightbooking.feature.flight.service.FlightService;
import io.github.rosstxix.flightbooking.feature.user.repository.UserRepository;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.SeatAlreadyBookedApiException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PaymentService paymentService;
    private final FlightService flightService;
    private final UserRepository userRepository;

    public BookingService(
            BookingRepository bookingRepository,
            PaymentService paymentService,
            FlightService flightService,
            UserRepository userRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.paymentService = paymentService;
        this.flightService = flightService;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createBooking(
            Long userId,
            Long flightId,
            String seatNumber
    ) {

        Flight flight = flightService.getFlightByIdForUpdate(flightId);
        flight.decrementAvailableSeats();

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
