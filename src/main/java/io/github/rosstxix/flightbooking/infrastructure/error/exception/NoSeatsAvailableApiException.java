package io.github.rosstxix.flightbooking.infrastructure.error.exception;

import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import org.springframework.http.HttpStatus;

public class NoSeatsAvailableApiException extends ApiException {

    public NoSeatsAvailableApiException(String message) {
        super(
                ApiErrorCode.CONFLICT,
                HttpStatus.CONFLICT,
                message
        );
    }
}
