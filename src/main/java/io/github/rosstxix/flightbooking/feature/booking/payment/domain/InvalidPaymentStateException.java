package io.github.rosstxix.flightbooking.feature.booking.payment.domain;

public class InvalidPaymentStateException extends RuntimeException {
    public InvalidPaymentStateException(String message) {
        super(message);
    }
}
