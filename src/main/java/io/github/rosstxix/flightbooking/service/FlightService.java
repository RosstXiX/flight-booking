package io.github.rosstxix.flightbooking.service;

import io.github.rosstxix.flightbooking.dto.response.FlightSearchResponse;
import io.github.rosstxix.flightbooking.dto.request.FlightSearchRequest;
import io.github.rosstxix.flightbooking.domain.entity.Flight;
import io.github.rosstxix.flightbooking.mapper.FlightMapper;
import io.github.rosstxix.flightbooking.repository.FlightRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;

    public FlightService(FlightRepository flightRepository, FlightMapper flightMapper) {
        this.flightRepository = flightRepository;
        this.flightMapper = flightMapper;
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
                .map(flightMapper::toSearchResponse)
                .collect(Collectors.toList());
    }

    public FlightSearchResponse getFlightDetails(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found"));
        return flightMapper.toSearchResponse(flight);
    }

}
