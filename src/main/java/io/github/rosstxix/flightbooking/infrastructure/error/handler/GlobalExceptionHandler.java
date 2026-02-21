package io.github.rosstxix.flightbooking.infrastructure.error.handler;

import io.github.rosstxix.flightbooking.infrastructure.error.ErrorResponseFactory;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.ApiException;
import io.github.rosstxix.flightbooking.infrastructure.logging.LogMessageFormatter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ErrorResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final ErrorResponseFactory errorResponseFactory;

    public GlobalExceptionHandler(ErrorResponseFactory errorResponseFactory) {
        this.errorResponseFactory = errorResponseFactory;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            HttpServletRequest req
    ) {
        log.error(LogMessageFormatter.handlerError(ApiErrorCode.INTERNAL_SERVER_ERROR, req, ex.getMessage()), ex);

        ErrorResponse response = errorResponseFactory.internalError(req.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(
            ApiException ex,
            HttpServletRequest request
    ) {
        log.warn(LogMessageFormatter.handlerError(ex.getErrorCode(), request, ex.getMessage()));

        ErrorResponse response = errorResponseFactory.create(
                ex.getHttpStatus(),
                ex.getErrorCode(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        BindingResult bindingResult = ex.getBindingResult();

        String message = bindingResult
                .getFieldErrors()
                .stream()
                .sorted(Comparator.comparing(FieldError::getField))
                .map(error -> {
                    String field = error.getField();

                    if (error.isBindingFailure()) {
                        Class<?> expectedType = bindingResult.getFieldType(field);
                        String typeName = expectedType != null
                                ? expectedType.getSimpleName()
                                : "unknown";

                        return "%s : must be of type %s".formatted(field, typeName);
                    }

                    return "%s : %s".formatted(field, error.getDefaultMessage());

                })
                .collect(Collectors.joining("; "));

        log.warn(LogMessageFormatter.handlerError(ApiErrorCode.VALIDATION_ERROR, request, message));

        ErrorResponse response = errorResponseFactory.validationError(
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(violation -> {
                    String field = violation.getPropertyPath().toString();

                    // getFlightDetails.id -> id
                    String param = field.contains(".")
                            ? field.substring(field.lastIndexOf(".") + 1)
                            : field;

                    return param + " : " + violation.getMessage();
                })
                .collect(Collectors.joining("; "));

        log.warn(LogMessageFormatter.handlerError(ApiErrorCode.VALIDATION_ERROR, request, message));

        ErrorResponse response = errorResponseFactory.validationError(
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        String field = ex.getName();

        String typeName = ex.getRequiredType() != null
                ? ex.getRequiredType().getSimpleName()
                : "unknown";

        String message = "%s : must be of type %s".formatted(field, typeName);

        log.warn(LogMessageFormatter.handlerError(ApiErrorCode.VALIDATION_ERROR, request, message));

        ErrorResponse response = errorResponseFactory.validationError(
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {
        String message = ex.getMessage();

        log.warn(LogMessageFormatter.handlerError(ApiErrorCode.BAD_CREDENTIALS, request, message));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseFactory.loginPassAuthError(request.getRequestURI()));
    }
}
