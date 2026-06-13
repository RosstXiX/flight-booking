package io.github.rosstxix.flightbooking.feature.flight.service;

import io.github.rosstxix.flightbooking.common.dto.PageResponse;
import io.github.rosstxix.flightbooking.feature.catalog.airport.service.AirportService;
import io.github.rosstxix.flightbooking.feature.flight.domain.FlightStatus;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import io.github.rosstxix.flightbooking.feature.flight.dto.projection.FlightProjection;
import io.github.rosstxix.flightbooking.feature.flight.dto.response.FlightSearchResponse;
import io.github.rosstxix.flightbooking.feature.flight.dto.request.FlightSearchRequest;
import io.github.rosstxix.flightbooking.feature.flight.mapper.FlightMapper;
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
public class FlightService {
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final AirportService airportService;

    public FlightService(
            FlightRepository flightRepository,
            FlightMapper flightMapper,
            AirportService airportService
    ) {
        this.flightRepository = flightRepository;
        this.flightMapper = flightMapper;
        this.airportService = airportService;
    }

    @Transactional(readOnly = true)
    public PageResponse<FlightSearchResponse> searchFlights(FlightSearchRequest request, Pageable pageable) {
        // Convert LocalDate to UTC diapason for searching

        ZoneId zone = ZoneId.of(airportService.getTimeZoneByCode(request.fromCode()));

        Instant startUtc = request.date().atStartOfDay(zone).toInstant();
        Instant endUtc = request.date().plusDays(1).atStartOfDay(zone).toInstant();

        Page<FlightProjection> projections = flightRepository.searchFlights(
                request.fromCode(),
                request.toCode(),
                startUtc,
                endUtc,
                FlightStatus.SCHEDULED,
                pageable
        );

        Page<FlightSearchResponse> response = projections.map(flightMapper::toSearchResponse);
        return PageResponse.from(response);
    }

    @Transactional(readOnly = true)
    public FlightSearchResponse getFlightDetails(Long id) {
        FlightProjection projection = flightRepository.findProjectionById(id)
                .orElseThrow(() ->
                        new EntityNotFoundApiException("Flight with id %d not found".formatted(id))
                );

        return flightMapper.toSearchResponse(projection);
    }

}
