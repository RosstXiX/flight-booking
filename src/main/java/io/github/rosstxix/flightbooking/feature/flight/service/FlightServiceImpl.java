package io.github.rosstxix.flightbooking.feature.flight.service;

import io.github.rosstxix.flightbooking.infrastructure.logging.aspect.Loggable;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import io.github.rosstxix.flightbooking.domain.Airport;
import io.github.rosstxix.flightbooking.feature.flight.dto.projection.FlightProjection;
import io.github.rosstxix.flightbooking.feature.flight.dto.response.FlightSearchResponse;
import io.github.rosstxix.flightbooking.feature.flight.dto.request.FlightSearchRequest;
import io.github.rosstxix.flightbooking.feature.flight.mapper.FlightMapper;
import io.github.rosstxix.flightbooking.repository.AirportRepository;
import io.github.rosstxix.flightbooking.feature.flight.repository.FlightRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;

@Slf4j
@Service
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final AirportRepository airportRepository;

    public FlightServiceImpl(
            FlightRepository flightRepository,
            FlightMapper flightMapper,
            AirportRepository airportRepository
    ) {
        this.flightRepository = flightRepository;
        this.flightMapper = flightMapper;
        this.airportRepository = airportRepository;
    }

    @Loggable
    @Transactional(readOnly = true)
    public Page<FlightSearchResponse> searchFlights(FlightSearchRequest request, Pageable pageable) {
        // Convert LocalDate to UTC diapason for searching

        Airport departureAirport = airportRepository.findByCode(request.fromCode())
                .orElseThrow(() ->
                    new EntityNotFoundApiException("Airport with code %s not found".formatted(request.fromCode()))
                );

        ZoneId zone = ZoneId.of(departureAirport.getTimeZone());

        Instant startUtc = request.date().atStartOfDay(zone).toInstant();
        Instant endUtc = request.date().plusDays(1).atStartOfDay(zone).toInstant();

        Page<FlightProjection> projections = flightRepository.searchFlights(
                request.fromCode(),
                request.toCode(),
                startUtc,
                endUtc,
                pageable
        );

        return projections.map(flightMapper::toSearchResponse);
    }

    @Loggable
    @Transactional(readOnly = true)
    public FlightSearchResponse getFlightDetails(Long id) {
        FlightProjection projection = flightRepository.findProjectionById(id)
                .orElseThrow(() ->
                    new EntityNotFoundApiException("Flight with id %d not found".formatted(id))
                );

        return flightMapper.toSearchResponse(projection);
    }

}
