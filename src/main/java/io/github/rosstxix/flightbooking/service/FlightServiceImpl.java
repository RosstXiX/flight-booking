package io.github.rosstxix.flightbooking.service;

import io.github.rosstxix.flightbooking.common.exception.EntityNotFoundApiException;
import io.github.rosstxix.flightbooking.domain.Airport;
import io.github.rosstxix.flightbooking.dto.projection.FlightProjection;
import io.github.rosstxix.flightbooking.dto.response.FlightSearchResponse;
import io.github.rosstxix.flightbooking.dto.request.FlightSearchRequest;
import io.github.rosstxix.flightbooking.mapper.FlightMapper;
import io.github.rosstxix.flightbooking.repository.AirportRepository;
import io.github.rosstxix.flightbooking.repository.FlightRepository;
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

    @Transactional(readOnly = true)
    public Page<FlightSearchResponse> searchFlights(FlightSearchRequest request, Pageable pageable) {
        log.info("Searching flights: from={}, to={}, date={}",
                request.fromCode(), request.toCode(), request.date());

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

        log.debug("Found {} flights for request", projections.getTotalElements());
        return projections.map(flightMapper::toSearchResponse);
    }

    @Transactional(readOnly = true)
    public FlightSearchResponse getFlightDetails(Long id) {
        log.info("Searching flight details: id={}", id);

        FlightProjection projection = flightRepository.findProjectionById(id)
                .orElseThrow(() ->
                    new EntityNotFoundApiException("Flight with id %d not found".formatted(id))
                );

        log.debug("Found flight details for id={}", id);
        return flightMapper.toSearchResponse(projection);
    }

}
