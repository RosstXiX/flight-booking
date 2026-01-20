package io.github.rosstxix.flightbooking.service;

import io.github.rosstxix.flightbooking.dto.response.FlightSearchResponse;
import io.github.rosstxix.flightbooking.dto.request.FlightSearchRequest;
import io.github.rosstxix.flightbooking.domain.entity.Flight;
import io.github.rosstxix.flightbooking.repository.FlightRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<FlightSearchResponse> searchFlights(FlightSearchRequest request) {
        // Convert LocalDate to UTC diapason for searching
        // TODO: Search by departure airport timezone, not UTC
        Instant startUtc = request.date().atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant endUtc = request.date().plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant();

        List<Flight> flights = flightRepository.searchFlights(
                request.fromCode(),
                request.toCode(),
                startUtc,
                endUtc
        );

        return flights.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FlightSearchResponse getFlightDetails(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found"));
        return convertToDTO(flight);
    }

    private FlightSearchResponse convertToDTO(Flight flight) {

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
