package io.github.rosstxix.flightbooking.service;

import io.github.rosstxix.flightbooking.dto.FlightDTO;
import io.github.rosstxix.flightbooking.dto.FlightSearchRequest;
import io.github.rosstxix.flightbooking.entity.Flight;
import io.github.rosstxix.flightbooking.repository.FlightRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<FlightDTO> searchFlights(FlightSearchRequest request) {
        // Convert LocalDate to UTC diapason for searching
        // TODO: Search by departure airport timezone, not UTC
        Instant startUtc = request.getDate().atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant endUtc = request.getDate().plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant();

        List<Flight> flights = flightRepository.searchFlights(
                request.getFromCode(),
                request.getToCode(),
                startUtc,
                endUtc
        );

        return flights.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FlightDTO getFlightDetails(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found"));
        return convertToDTO(flight);
    }


    private FlightDTO convertToDTO(Flight flight) {
        FlightDTO dto = new FlightDTO();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());

        // Airports
        dto.setDepartureAirportCode(flight.getDepartureAirport().getCode());
        dto.setDepartureAirportCity(flight.getDepartureAirport().getCity());
        dto.setArrivalAirportCode(flight.getArrivalAirport().getCode());
        dto.setArrivalAirportCity(flight.getArrivalAirport().getCity());

        // UTC Instant -> local airport time
        LocalDateTime departureLocal = LocalDateTime.ofInstant(
                flight.getDepartureUtc(),
                ZoneId.of(flight.getDepartureAirport().getTimeZone())
        );
        dto.setDepartureLocalTime(departureLocal);

        LocalDateTime arrivalLocal = LocalDateTime.ofInstant(
                flight.getArrivalUtc(),
                ZoneId.of(flight.getArrivalAirport().getTimeZone())
        );
        dto.setArrivalLocalTime(arrivalLocal);

        // Aircraft
        dto.setAircraftModel(flight.getAircraft().getModel());
        dto.setTotalSeats(flight.getAircraft().getTotalSeats());

        dto.setPrice(flight.getPrice());
        dto.setStatus(flight.getStatus().toString());

        // Flight duration
        Duration duration = Duration.between(flight.getDepartureUtc(), flight.getArrivalUtc());
        dto.setDurationMinutes(duration.toMinutes());

        return dto;
    }

}
