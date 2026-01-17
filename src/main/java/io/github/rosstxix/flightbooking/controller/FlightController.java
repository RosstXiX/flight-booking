package io.github.rosstxix.flightbooking.controller;

import io.github.rosstxix.flightbooking.dto.FlightDTO;
import io.github.rosstxix.flightbooking.dto.FlightSearchRequest;
import io.github.rosstxix.flightbooking.entity.Flight;
import io.github.rosstxix.flightbooking.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightDTO>> searchFlights(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        FlightSearchRequest request = new FlightSearchRequest(from, to, date);
        List<FlightDTO> flights = flightService.searchFlights(request);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> getFlightDetails(@PathVariable Long id) {
        FlightDTO flight = flightService.getFlightDetails(id);
        return ResponseEntity.ok(flight);
    }
}
