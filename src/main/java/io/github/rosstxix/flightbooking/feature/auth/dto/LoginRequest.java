package io.github.rosstxix.flightbooking.feature.auth.dto;

public record LoginRequest (
        String email,
        String password
) {

}
