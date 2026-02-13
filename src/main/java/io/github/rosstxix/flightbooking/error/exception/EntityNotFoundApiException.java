package io.github.rosstxix.flightbooking.error.exception;

import io.github.rosstxix.flightbooking.error.model.ApiErrorCode;
import org.springframework.http.HttpStatus;

public class EntityNotFoundApiException extends ApiException {

    public EntityNotFoundApiException(String message) {
        super(
                ApiErrorCode.ENTITY_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                message
        );
    }
}
