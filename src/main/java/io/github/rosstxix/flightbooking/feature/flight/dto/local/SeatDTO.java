package io.github.rosstxix.flightbooking.feature.flight.dto.local;

import io.swagger.v3.oas.annotations.media.Schema;

public record SeatDTO(
        @Schema(example = "1A", requiredMode = Schema.RequiredMode.REQUIRED)
        String seatNumber,
        @Schema(example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        boolean isOccupied
) {

}
