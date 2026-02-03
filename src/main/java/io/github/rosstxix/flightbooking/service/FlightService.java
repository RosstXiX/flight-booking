package io.github.rosstxix.flightbooking.service;

import io.github.rosstxix.flightbooking.dto.request.FlightSearchRequest;
import io.github.rosstxix.flightbooking.dto.response.FlightSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FlightService {
    Page<FlightSearchResponse> searchFlights(
            FlightSearchRequest request,
            Pageable pageable
    );

    FlightSearchResponse getFlightDetails(Long id);
}
