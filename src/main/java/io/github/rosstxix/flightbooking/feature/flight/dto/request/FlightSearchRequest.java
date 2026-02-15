package io.github.rosstxix.flightbooking.feature.flight.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Flight search criteria")
public record FlightSearchRequest(
        @NotBlank(message = "Departure airport code is required")
        @Pattern(
                regexp = "^[A-Z]{3}$",
                message = "Departure airport code must be a valid IATA code (3 uppercase letters)"
        )
        @Schema(description = "Code of the departure airport (IATA)", example = "KBP")
        String fromCode,

        @NotBlank(message = "Arrival airport code is required")
        @Pattern(
                regexp = "^[A-Z]{3}$",
                message = "Arrival airport code must be a valid IATA code (3 uppercase letters)"
        )
        @Schema(description = "Code of the arrival airport (IATA)", example = "LWO")
        String toCode,

        @NotNull(message = "Departure date is required")
        @FutureOrPresent(message = "Departure date must be today or in the future")
        @Schema(description = "Departure date", example = "2026-02-20")
        LocalDate date
) {
    @Schema(hidden = true)
    @AssertTrue(message = "Departure and arrival airports must be different")
    public boolean isDifferentAirports() {
        if (fromCode == null || toCode == null) {
            return true;
        }

        return !fromCode.equals(toCode);
    }

}
