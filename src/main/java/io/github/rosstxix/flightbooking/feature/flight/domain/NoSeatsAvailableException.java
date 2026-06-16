package io.github.rosstxix.flightbooking.feature.flight.domain;

public class NoSeatsAvailableException extends RuntimeException {
    public NoSeatsAvailableException(String message) {
        super(message);
    }
}
