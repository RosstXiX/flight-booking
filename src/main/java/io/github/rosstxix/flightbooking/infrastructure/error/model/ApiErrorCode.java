package io.github.rosstxix.flightbooking.infrastructure.error.model;

public enum ApiErrorCode {

    // 400
    VALIDATION_ERROR,

    // 401
    AUTHENTICATION_FAILED,
    TOKEN_EXPIRED,
    TOKEN_INVALID,
    TOKEN_MISSING,

    // 403
    ACCESS_DENIED,

    // 404
    ENTITY_NOT_FOUND,

    // 500
    INTERNAL_SERVER_ERROR
}
