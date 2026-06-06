package io.github.rosstxix.flightbooking.feature.flight.dto.local;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record SeatRowDTO(
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        int rowNumber,
        @Schema(example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        boolean isPremium,
        @Schema(description = "List of seats in the row", requiredMode = Schema.RequiredMode.REQUIRED)
        List<SeatDTO> seats
) {

}
