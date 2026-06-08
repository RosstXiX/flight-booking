package io.github.rosstxix.flightbooking.feature.booking.dto.request;

public record BookingCreateRequest(
        Long flightId,
        String seatNumber
) {
}
