package io.github.rosstxix.flightbooking.feature.catalog.airport.service;

import io.github.rosstxix.flightbooking.feature.catalog.airport.repository.AirportRepository;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Transactional(readOnly = true)
    public String findTimeZoneByCode(String code) {
        return airportRepository.findTimeZoneByCode(code).orElseThrow(
                () -> new EntityNotFoundApiException("Airport with code %s not found".formatted(code))
        );
    }
}
