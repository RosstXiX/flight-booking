package io.github.rosstxix.flightbooking.common.web;

import io.github.rosstxix.flightbooking.common.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import io.github.rosstxix.flightbooking.common.error.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(
            ApiException ex,
            HttpServletRequest request
    ) {
        ErrorResponse response = new ErrorResponse(
                ex.getHttpStatus(),
                ex.getErrorCode(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }
}
