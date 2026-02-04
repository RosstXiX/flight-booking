package io.github.rosstxix.flightbooking.dto.projection;

import io.github.rosstxix.flightbooking.domain.FlightStatus;

import java.math.BigDecimal;
import java.time.Instant;

public interface FlightProjection {
    Long getId();
    String getFlightNumber();
    BigDecimal getPrice();
    FlightStatus getStatus();

    // Airport entity
    String getDepartureAirportCode();
    String getDepartureAirportCity();
    String getDepartureAirportTimeZone();
    Instant getDepartureUtc();

    String getArrivalAirportTimeZone();
    String getArrivalAirportCode();
    String getArrivalAirportCity();
    Instant getArrivalUtc();

    // Aircraft entity
    String getAircraftModel();
    Integer getTotalSeats();


}
