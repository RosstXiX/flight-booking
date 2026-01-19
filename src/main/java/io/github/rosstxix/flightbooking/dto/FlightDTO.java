package io.github.rosstxix.flightbooking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightDTO(
        Long id,
        String flightNumber,
        String departureAirportCode,
        String departureAirportCity,
        String arrivalAirportCode,
        String arrivalAirportCity,
        LocalDateTime departureLocalTime,  // Local time of departure airport
        LocalDateTime arrivalLocalTime,    // Local time of arrival airport
        String aircraftModel,
        Integer totalSeats,
        BigDecimal price,
        String status,
        Long durationMinutes
) {

}
