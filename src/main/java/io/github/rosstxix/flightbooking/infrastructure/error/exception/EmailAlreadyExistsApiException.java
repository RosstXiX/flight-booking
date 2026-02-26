package io.github.rosstxix.flightbooking.infrastructure.error.exception;

import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsApiException extends ApiException {

    public EmailAlreadyExistsApiException(String message) {
        super(
                ApiErrorCode.CONFLICT,
                HttpStatus.CONFLICT,
                message
        );
    }
}
