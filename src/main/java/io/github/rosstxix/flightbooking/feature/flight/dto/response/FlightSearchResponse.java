package io.github.rosstxix.flightbooking.feature.flight.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Flight search result")
public record FlightSearchResponse(
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "Unique flight number", example = "PS101", requiredMode = Schema.RequiredMode.REQUIRED)
        String flightNumber,
        @Schema(description = "Code of the departure airport(IATA)", example = "KBP", requiredMode = Schema.RequiredMode.REQUIRED)
        String departureAirportCode,
        @Schema(example = "Kiev", requiredMode = Schema.RequiredMode.REQUIRED)
        String departureAirportCity,
        @Schema(description = "Code of the arrival airport(IATA)", example = "LWO", requiredMode = Schema.RequiredMode.REQUIRED)
        String arrivalAirportCode,
        @Schema(example = "Lviv", requiredMode = Schema.RequiredMode.REQUIRED)
        String arrivalAirportCity,
        @Schema(description = "Date and time of departure taking into account timezone", example = "2026-02-20T08:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime departureLocalTime,  // Local time of departure airport
        @Schema(description = "Date and time of arrival taking into account timezone", example = "2026-02-20T09:15:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime arrivalLocalTime,    // Local time of arrival airport
        @Schema(example = "Boeing 737-800", requiredMode = Schema.RequiredMode.REQUIRED)
        String aircraftModel,
        @Schema(description = "Total number of seats on the aircraft", example = "189", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer totalSeats,
        @Schema(example = "1500.00", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal price,
        @Schema(example = "SCHEDULED", requiredMode = Schema.RequiredMode.REQUIRED)
        String status,
        @Schema(example = "75", requiredMode = Schema.RequiredMode.REQUIRED)
        Long durationMinutes
) {

}
