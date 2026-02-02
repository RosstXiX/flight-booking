package io.github.rosstxix.flightbooking.controller;

import io.github.rosstxix.flightbooking.common.error.ErrorResponse;
import io.github.rosstxix.flightbooking.dto.response.FlightSearchResponse;
import io.github.rosstxix.flightbooking.dto.request.FlightSearchRequest;
import io.github.rosstxix.flightbooking.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@Tag(name = "Flights", description = "Flight management and search")
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @Operation(
            summary = "Flight search",
            description = "Allows you to find available flights by airport codes and departure date"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Flights successfully found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FlightSearchResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid query parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "One of the listed airports was not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Search criteria", required = true)
    @GetMapping("/search")
    public ResponseEntity<Page<FlightSearchResponse>> searchFlights(
            @Valid @ModelAttribute FlightSearchRequest request,
            @ParameterObject Pageable pageable
    ) {
        Page<FlightSearchResponse> flights = flightService.searchFlights(request, pageable);
        return ResponseEntity.ok(flights);
    }

    @Operation(
            summary = "Get flight details",
            description = "Retrieves detailed information about a specific flight by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Flight details successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FlightSearchResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid flight ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Flight with the specified ID was not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<FlightSearchResponse> getFlightDetails(
            @PathVariable
            @Parameter(example = "5", required = true)
            @Positive(message = "Flight id must be greater than zero")
            Long id
    ) {
        FlightSearchResponse flight = flightService.getFlightDetails(id);
        return ResponseEntity.ok(flight);
    }
}
