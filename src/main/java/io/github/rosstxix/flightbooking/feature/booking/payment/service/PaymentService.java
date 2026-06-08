package io.github.rosstxix.flightbooking.feature.booking.payment.service;

import io.github.rosstxix.flightbooking.feature.booking.domain.Booking;
import io.github.rosstxix.flightbooking.feature.booking.payment.domain.Payment;
import io.github.rosstxix.flightbooking.feature.booking.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public void createPayment(Booking booking) {
        Payment payment = new Payment(
                booking.getFlight().getPrice(),
                booking.getFlight().getCurrency(),
                booking
        );

        paymentRepository.save(payment);
    }
}
