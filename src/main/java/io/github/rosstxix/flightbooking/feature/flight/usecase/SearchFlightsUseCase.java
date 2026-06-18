package io.github.rosstxix.flightbooking.feature.flight.usecase;

import io.github.rosstxix.flightbooking.common.dto.PageResponse;
import io.github.rosstxix.flightbooking.feature.catalog.airport.service.AirportService;
import io.github.rosstxix.flightbooking.feature.flight.domain.FlightStatus;
import io.github.rosstxix.flightbooking.feature.flight.dto.projection.FlightProjection;
import io.github.rosstxix.flightbooking.feature.flight.dto.request.FlightSearchRequest;
import io.github.rosstxix.flightbooking.feature.flight.dto.response.FlightSearchResponse;
import io.github.rosstxix.flightbooking.feature.flight.mapper.FlightMapper;
import io.github.rosstxix.flightbooking.feature.flight.repository.FlightRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;

@Component
public class SearchFlightsUseCase {

    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final AirportService airportService;

    public SearchFlightsUseCase(
            FlightRepository flightRepository,
            FlightMapper flightMapper,
            AirportService airportService
    ) {
        this.flightRepository = flightRepository;
        this.flightMapper = flightMapper;
        this.airportService = airportService;
    }

    @Transactional(readOnly = true)
    public PageResponse<FlightSearchResponse> execute(FlightSearchRequest request, Pageable pageable) {
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
}
