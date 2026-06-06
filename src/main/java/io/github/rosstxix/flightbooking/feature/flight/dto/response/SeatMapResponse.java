package io.github.rosstxix.flightbooking.feature.flight.dto.response;

import io.github.rosstxix.flightbooking.feature.flight.dto.local.SeatRowDTO;

import java.util.List;

public record SeatMapResponse(
        String aircraftModel,
        Integer totalSeats,
        String seatLayout,
        String premiumSeatLayout,
        List<SeatRowDTO> seats
) {

}
