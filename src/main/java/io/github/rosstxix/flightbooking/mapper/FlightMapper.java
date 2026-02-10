package io.github.rosstxix.flightbooking.mapper;

import io.github.rosstxix.flightbooking.dto.projection.FlightProjection;
import io.github.rosstxix.flightbooking.dto.response.FlightSearchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface FlightMapper {

    @Mapping(
            target = "departureLocalTime",
            expression = "java(toLocal(projection.getDepartureUtc(), projection.getDepartureAirportTimeZone()))"
    )
    @Mapping(
            target = "arrivalLocalTime",
            expression = "java(toLocal(projection.getArrivalUtc(), projection.getArrivalAirportTimeZone()))"
    )
    @Mapping(
            target = "status",
            expression = "java(projection.getStatus().name())"
    )
    @Mapping(
            target = "durationMinutes",
            expression = "java(calculateDuration(projection.getDepartureUtc(), projection.getArrivalUtc()))"
    )
    FlightSearchResponse toSearchResponse(FlightProjection projection);

    default LocalDateTime toLocal(Instant instant, String timeZone) {
        return LocalDateTime.ofInstant(instant, ZoneId.of(timeZone));
    }

    default Long calculateDuration(Instant departure, Instant arrival) {
        return Duration.between(departure, arrival).toMinutes();
    }
}
