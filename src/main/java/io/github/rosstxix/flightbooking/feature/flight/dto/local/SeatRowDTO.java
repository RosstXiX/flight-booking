package io.github.rosstxix.flightbooking.feature.flight.dto.local;

import java.util.List;

public record SeatRowDTO(
        int rowNumber,
        boolean isPremium,
        List<SeatDTO> seats
) {

}
