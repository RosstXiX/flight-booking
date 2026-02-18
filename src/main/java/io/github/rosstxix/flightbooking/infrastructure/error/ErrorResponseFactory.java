package io.github.rosstxix.flightbooking.infrastructure.error;

import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponseFactory {

    public ErrorResponse create(HttpStatus status, ApiErrorCode error, String message, String path) {
        return new ErrorResponse(status, error, message, path);
    }

    public ErrorResponse internalError(String path) {
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ApiErrorCode.INTERNAL_SERVER_ERROR,
                "Unexpected error occurred",
                path
        );
    }

    public ErrorResponse validationError(String message, String path) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                ApiErrorCode.VALIDATION_ERROR,
                message,
                path
        );
    }
}
