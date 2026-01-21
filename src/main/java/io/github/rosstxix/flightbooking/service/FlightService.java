package io.github.rosstxix.flightbooking.service;

import io.github.rosstxix.flightbooking.common.exception.EntityNotFoundApiException;
import io.github.rosstxix.flightbooking.domain.entity.Airport;
import io.github.rosstxix.flightbooking.dto.response.FlightSearchResponse;
import io.github.rosstxix.flightbooking.dto.request.FlightSearchRequest;
import io.github.rosstxix.flightbooking.domain.entity.Flight;
import io.github.rosstxix.flightbooking.mapper.FlightMapper;
import io.github.rosstxix.flightbooking.repository.AirportRepository;
import io.github.rosstxix.flightbooking.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final AirportRepository airportRepository;

    public FlightService(
            FlightRepository flightRepository,
            FlightMapper flightMapper,
            AirportRepository airportRepository
    ) {
        this.flightRepository = flightRepository;
        this.flightMapper = flightMapper;
        this.airportRepository = airportRepository;
    }

    public List<FlightSearchResponse> searchFlights(FlightSearchRequest request) {
        // Convert LocalDate to UTC diapason for searching

        Airport departureAirport = airportRepository.findByCode(request.fromCode())
                .orElseThrow(() -> new EntityNotFoundApiException("Airport with code %s not found".formatted(request.fromCode())));

        ZoneId zone = ZoneId.of(departureAirport.getTimeZone());

        Instant startUtc = request.date().atStartOfDay(zone).toInstant();
        Instant endUtc = request.date().plusDays(1).atStartOfDay(zone).toInstant();

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
                .orElseThrow(() -> new EntityNotFoundApiException("Flight with id %d not found".formatted(id)));
        return flightMapper.toSearchResponse(flight);
    }

}
