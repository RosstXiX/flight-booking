package io.github.rosstxix.flightbooking.infrastructure.error.exception;

import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import org.springframework.http.HttpStatus;

public class SeatAlreadyBookedApiException extends ApiException {

    public SeatAlreadyBookedApiException(String message) {
        super(
                ApiErrorCode.SEAT_ALREADY_BOOKED,
                HttpStatus.CONFLICT,
                message
        );
    }
}
