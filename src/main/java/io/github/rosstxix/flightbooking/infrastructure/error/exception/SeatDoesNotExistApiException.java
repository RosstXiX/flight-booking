package io.github.rosstxix.flightbooking.infrastructure.error.exception;

import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import org.springframework.http.HttpStatus;

public class SeatDoesNotExistApiException extends ApiException {

    public SeatDoesNotExistApiException(String message) {
        super(
                ApiErrorCode.SEAT_DOES_NOT_EXIST,
                HttpStatus.UNPROCESSABLE_ENTITY,
                message
        );
    }
}
