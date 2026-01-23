package io.github.rosstxix.flightbooking.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record FlightSearchRequest(
        @NotBlank(message = "Departure airport code is required")
        @Pattern(
                regexp = "^[A-Z]{3}$",
                message = "Departure airport code must be a valid IATA code (3 uppercase letters)"
        )
        String fromCode,

        @NotBlank(message = "Arrival airport code is required")
        @Pattern(
                regexp = "^[A-Z]{3}$",
                message = "Arrival airport code must be a valid IATA code (3 uppercase letters)"
        )
        String toCode,

        @NotNull(message = "Departure date is required")
        @FutureOrPresent(message = "Departure date must be today or in the future")
        LocalDate date
) {
    @AssertTrue(message = "Departure and arrival airports must be different")
    public boolean isDifferentAirports() {
        if (fromCode == null || toCode == null) {
            return true;
        }

        return !fromCode.equals(toCode);
    }

}
