package io.github.rosstxix.flightbooking.feature.booking.domain;

public class InvalidBookingStateException extends RuntimeException {
    public InvalidBookingStateException(String message) {
        super(message);
    }
}
