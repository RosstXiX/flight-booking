package io.github.rosstxix.flightbooking.feature.flight.dto.response;

import io.github.rosstxix.flightbooking.feature.flight.dto.local.SeatRowDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record SeatMapResponse(
        @Schema(example = "Boeing 747-8", requiredMode = Schema.RequiredMode.REQUIRED)
        String aircraftModel,
        @Schema(example = "160", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer totalSeats,
        @Schema(description = "Information about seat layout", example = "ABC_EDF", requiredMode = Schema.RequiredMode.REQUIRED)
        String seatLayout,
        @Schema(description = "Information about premium seat layout", example = "AB_CD", requiredMode = Schema.RequiredMode.REQUIRED)
        String premiumSeatLayout,
        @Schema(description = "List of seat rows", requiredMode = Schema.RequiredMode.REQUIRED)
        List<SeatRowDTO> seats
) {

}
