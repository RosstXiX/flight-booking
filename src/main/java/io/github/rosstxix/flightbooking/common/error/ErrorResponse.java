package io.github.rosstxix.flightbooking.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
public class ErrorResponse {

    private final Instant timestamp = Instant.now();
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public ErrorResponse(
            HttpStatus status,
            ApiErrorCode error,
            String message,
            String path
    ) {
        this.status = status.value();
        this.error = error.toString();
        this.message = message;
        this.path = path;
    }
}
