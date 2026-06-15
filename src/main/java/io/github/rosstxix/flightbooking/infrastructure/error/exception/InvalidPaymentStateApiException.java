package io.github.rosstxix.flightbooking.infrastructure.error.exception;

import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidPaymentStateApiException extends ApiException {

    public InvalidPaymentStateApiException(String message) {
        super(
                ApiErrorCode.INVALID_BOOKING_STATE,
                HttpStatus.CONFLICT,
                message
        );
    }
}
