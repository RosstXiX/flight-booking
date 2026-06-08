package io.github.rosstxix.flightbooking.infrastructure.error.exception;

import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidBookingStateApiException extends ApiException {

    public InvalidBookingStateApiException(String message) {
        super(
                ApiErrorCode.INVALID_BOOKING_STATE,
                HttpStatus.CONFLICT,
                message
        );
    }
}
