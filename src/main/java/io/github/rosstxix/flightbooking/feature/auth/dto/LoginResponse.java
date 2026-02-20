package io.github.rosstxix.flightbooking.feature.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login response")
public record LoginResponse (
        @Schema(description = "JWT access token", example = "eyJhbGci...", requiredMode = Schema.RequiredMode.REQUIRED)
        String accessToken,

        @Schema(description = "Token type", example = "Bearer", requiredMode = Schema.RequiredMode.REQUIRED)
        String tokenType,

        @Schema(description = "Token expiration time in seconds", example = "3600", requiredMode = Schema.RequiredMode.REQUIRED)
        long expiresIn
) {
}
