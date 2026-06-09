package io.github.rosstxix.flightbooking.feature.booking.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record BookingCreateRequest(
        @NotNull(message = "Flight ID is required")
        @Positive(message = "Flight ID must be a positive number")
        Long flightId,

        @NotBlank(message = "Seat number is required")
        @Pattern(
                regexp = "\\d{1,2}[A-Z]",
                message = "Seat number must be in the format '1A', '12B', etc"
        )
        String seatNumber
) {
}
