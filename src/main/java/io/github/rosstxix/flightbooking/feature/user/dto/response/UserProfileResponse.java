package io.github.rosstxix.flightbooking.feature.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User profile response")
public record UserProfileResponse(
        @Schema(example = "john@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,
        @Schema(example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
        String firstName,
        @Schema(example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
        String lastName
) {
}
