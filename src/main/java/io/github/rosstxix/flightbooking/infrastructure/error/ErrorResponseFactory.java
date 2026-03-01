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

}
