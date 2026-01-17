package io.github.rosstxix.flightbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FlightDTO {
    private Long id;
    private String flightNumber;
    private String departureAirportCode;
    private String departureAirportCity;
    private String arrivalAirportCode;
    private String arrivalAirportCity;
    private LocalDateTime departureLocalTime;   // Local time of departure airport
    private LocalDateTime arrivalLocalTime;     // Local time of arrival airport
    private String aircraftModel;
    private Integer totalSeats;
    private BigDecimal price;
    private String status;
    private Long durationMinutes;
}
