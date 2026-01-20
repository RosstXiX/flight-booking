package io.github.rosstxix.flightbooking.controller;

import io.github.rosstxix.flightbooking.dto.response.FlightSearchResponse;
import io.github.rosstxix.flightbooking.dto.request.FlightSearchRequest;
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
    public ResponseEntity<List<FlightSearchResponse>> searchFlights(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        FlightSearchRequest request = new FlightSearchRequest(from, to, date);
        List<FlightSearchResponse> flights = flightService.searchFlights(request);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightSearchResponse> getFlightDetails(@PathVariable Long id) {
        FlightSearchResponse flight = flightService.getFlightDetails(id);
        return ResponseEntity.ok(flight);
    }
}
