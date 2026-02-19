package io.github.rosstxix.flightbooking.infrastructure.error.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiErrorCode {

    // 400
    VALIDATION_ERROR("Validation error"),

    // 401
    AUTHENTICATION_FAILED("Authentication failed"),
    TOKEN_EXPIRED("Token expired"),
    TOKEN_INVALID("Token invalid"),
    TOKEN_MISSING("Token missing"),

    // 403
    ACCESS_DENIED("Access denied"),

    // 404
    ENTITY_NOT_FOUND("Entity not found"),

    // 500
    INTERNAL_SERVER_ERROR("Unexpected error");

    private final String description;

}
