package io.github.rosstxix.flightbooking.feature.flight.usecase;

import io.github.rosstxix.flightbooking.feature.flight.dto.projection.FlightProjection;
import io.github.rosstxix.flightbooking.feature.flight.dto.response.FlightSearchResponse;
import io.github.rosstxix.flightbooking.feature.flight.mapper.FlightMapper;
import io.github.rosstxix.flightbooking.feature.flight.repository.FlightRepository;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GetFlightDetailsUseCase {

    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;

    public GetFlightDetailsUseCase(
            FlightRepository flightRepository,
            FlightMapper flightMapper
    ) {
        this.flightRepository = flightRepository;
        this.flightMapper = flightMapper;
    }

    @Transactional(readOnly = true)
    public FlightSearchResponse execute(Long id) {
        FlightProjection projection = flightRepository.findProjectionById(id)
                .orElseThrow(() ->
                        new EntityNotFoundApiException("Flight with id %d not found".formatted(id))
                );

        return flightMapper.toSearchResponse(projection);
    }
}
