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
    BAD_CREDENTIALS("Bad credentials"),
    INVALID_REQUEST_BODY("Invalid request body"),
    INVALID_JSON("Malformed JSON"),
    INVALID_FORMAT("Invalid request field format"),

    // 403
    ACCESS_DENIED("Access denied"),

    // 404
    ENTITY_NOT_FOUND("Entity not found"),

    // 409
    EMAIL_ALREADY_EXISTS("Email already exists"),
    SEAT_ALREADY_BOOKED("Seat already booked"),
    NO_SEATS_AVAILABLE("No seats available on this flight"),
    INVALID_BOOKING_STATE("Booking state transition is not allowed"),

    // 422
    SEAT_DOES_NOT_EXIST("Seat does not exist"),

    // 500
    INTERNAL_SERVER_ERROR("Unexpected error");

    private final String description;

}
