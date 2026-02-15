package io.github.rosstxix.flightbooking.infrastructure.error.exception;

import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {
    private final ApiErrorCode errorCode;
    private final HttpStatus httpStatus;

    protected ApiException(
            ApiErrorCode errorCode,
            HttpStatus status,
            String message
    ) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = status;
    }

}
