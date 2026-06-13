package io.github.rosstxix.flightbooking.feature.booking.payment.service;

import io.github.rosstxix.flightbooking.feature.booking.domain.Booking;
import io.github.rosstxix.flightbooking.feature.booking.payment.domain.Payment;
import io.github.rosstxix.flightbooking.feature.booking.payment.repository.PaymentRepository;
import io.github.rosstxix.flightbooking.feature.flight.domain.Flight;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @InjectMocks
    private PaymentService paymentService;

    @Test
    void should_createPaymentWithSuccessStatus_whenBookingProvided() {
        // Arrange
        Flight flight = new Flight(
                null, null, null, null, null, null,
                BigDecimal.valueOf(1000), null, "USD"
        );
        Booking booking = new Booking(null, flight, null);

        // Act
        paymentService.createPayment(booking);

        // Assert
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());

        Payment payment = paymentCaptor.getValue();
        assertThat(payment.getAmount()).isEqualTo(booking.getFlight().getPrice());
        assertThat(payment.getCurrency()).isEqualTo(booking.getFlight().getCurrency());
        assertThat(payment.getBooking()).isSameAs(booking);
    }
}
