package io.github.rosstxix.flightbooking.feature.flight.service;

import io.github.rosstxix.flightbooking.dto.PageResponse;
import io.github.rosstxix.flightbooking.feature.flight.dto.request.FlightSearchRequest;
import io.github.rosstxix.flightbooking.feature.flight.dto.response.FlightSearchResponse;
import org.springframework.data.domain.Pageable;

public interface FlightService {
    PageResponse<FlightSearchResponse> searchFlights(
            FlightSearchRequest request,
            Pageable pageable
    );

    FlightSearchResponse getFlightDetails(Long id);
}
