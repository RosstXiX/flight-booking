package io.github.rosstxix.flightbooking.dto;

import java.time.LocalDate;

public record FlightSearchRequest(
        String fromCode,
        String toCode,
        LocalDate date
) {

}
