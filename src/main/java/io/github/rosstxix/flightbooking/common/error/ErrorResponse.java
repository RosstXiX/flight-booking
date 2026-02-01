package io.github.rosstxix.flightbooking.common.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Schema(description = "Default error response")
public class ErrorResponse {

    @Schema(description = "Time of error occurrence", example = "2026-02-01T10:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Instant timestamp = Instant.now();
    @Schema(description = "HTTP status code", example = "400", requiredMode = Schema.RequiredMode.REQUIRED)
    private final int status;
    @Schema(description = "Error code", example = "VALIDATION_ERROR", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String error;
    @Schema(description = "Detailed error message", example = "fromCode : Departure airport code must be a valid IATA code (3 uppercase letters", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String message;
    @Schema(description = "Path of the request", example = "api/flights/search", requiredMode = Schema.RequiredMode.REQUIRED)
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
