package io.github.rosstxix.flightbooking.feature.booking.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Booking details response")
public record BookingDetailsResponse(
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(example = "CONFIRMED", requiredMode = Schema.RequiredMode.REQUIRED)
        String status,
        @Schema(example = "17B", requiredMode = Schema.RequiredMode.REQUIRED)
        String seatNumber,
        @Schema(example = "2026-06-16T10:30:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
        Instant createdAt,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        FlightInfo flight,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        PaymentInfo payment
) {
        @Schema(description = "Flight information")
        public record FlightInfo(
                @Schema(example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
                Long id,
                @Schema(example = "A1524", requiredMode = Schema.RequiredMode.REQUIRED)
                String flightNumber,
                @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
                AirportInfo departure,
                @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
                AirportInfo arrival,
                @Schema(example = "2026-07-01T14:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
                Instant departureUtc,
                @Schema(example = "2026-07-02T02:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
                Instant arrivalUtc,
                @Schema(example = "Boeing 777", requiredMode = Schema.RequiredMode.REQUIRED)
                String aircraftModel
        ) {
        }

        @Schema(description = "Airport information")
        public record AirportInfo(
                @Schema(example = "JFK", requiredMode = Schema.RequiredMode.REQUIRED)
                String code,
                @Schema(example = "New York", requiredMode = Schema.RequiredMode.REQUIRED)
                String city,
                @Schema(example = "USA", requiredMode = Schema.RequiredMode.REQUIRED)
                String country
        ) {
        }

        @Schema(description = "Payment information")
        public record PaymentInfo(
                @Schema(example = "450.00", requiredMode = Schema.RequiredMode.REQUIRED)
                BigDecimal amount,
                @Schema(example = "USD", requiredMode = Schema.RequiredMode.REQUIRED)
                String currency,
                @Schema(example = "SUCCESS", requiredMode = Schema.RequiredMode.REQUIRED)
                String status
        ) {
        }
}
