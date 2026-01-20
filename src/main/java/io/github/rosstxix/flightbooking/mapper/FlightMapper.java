package io.github.rosstxix.flightbooking.mapper;

import io.github.rosstxix.flightbooking.domain.entity.Flight;
import io.github.rosstxix.flightbooking.dto.response.FlightSearchResponse;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class FlightMapper {

    public FlightSearchResponse toSearchResponse(Flight flight) {

        // UTC Instant -> local airport time
        LocalDateTime departureLocal = LocalDateTime.ofInstant(
                flight.getDepartureUtc(),
                ZoneId.of(flight.getDepartureAirport().getTimeZone())
        );
        LocalDateTime arrivalLocal = LocalDateTime.ofInstant(
                flight.getArrivalUtc(),
                ZoneId.of(flight.getArrivalAirport().getTimeZone())
        );
        Duration duration = Duration.between(flight.getDepartureUtc(), flight.getArrivalUtc());

        return new FlightSearchResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getDepartureAirport().getCode(),
                flight.getDepartureAirport().getCity(),
                flight.getArrivalAirport().getCode(),
                flight.getArrivalAirport().getCity(),
                departureLocal,
                arrivalLocal,
                flight.getAircraft().getModel(),
                flight.getAircraft().getTotalSeats(),
                flight.getPrice(),
                flight.getStatus().toString(),
                duration.toMinutes()
        );
    }

}
