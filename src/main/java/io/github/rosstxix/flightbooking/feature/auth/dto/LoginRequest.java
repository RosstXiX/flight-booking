package io.github.rosstxix.flightbooking.feature.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Login credentials")
public record LoginRequest (
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email address")
        @Schema(description = "User email", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Schema(description = "User password", example = "secret123", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {

}
