package io.github.rosstxix.flightbooking.feature.flight.controller;

import io.github.rosstxix.flightbooking.common.dto.PageResponse;
import io.github.rosstxix.flightbooking.feature.flight.dto.response.SeatMapResponse;
import io.github.rosstxix.flightbooking.feature.flight.usecase.GetSeatMapUseCase;
import io.github.rosstxix.flightbooking.feature.flight.usecase.SearchFlightsUseCase;
import io.github.rosstxix.flightbooking.feature.flight.usecase.GetFlightDetailsUseCase;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ErrorResponse;
import io.github.rosstxix.flightbooking.feature.flight.dto.response.FlightSearchResponse;
import io.github.rosstxix.flightbooking.feature.flight.dto.request.FlightSearchRequest;
import io.github.rosstxix.flightbooking.infrastructure.openapi.ErrorApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Tag(name = "Flights", description = "Flight management and search")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final SearchFlightsUseCase searchFlightsUseCase;
    private final GetFlightDetailsUseCase getFlightDetailsUseCase;
    private final GetSeatMapUseCase getSeatMapUseCase;

    public FlightController(
            SearchFlightsUseCase searchFlightsUseCase,
            GetFlightDetailsUseCase getFlightDetailsUseCase,
            GetSeatMapUseCase getSeatMapUseCase
    ) {
        this.searchFlightsUseCase = searchFlightsUseCase;
        this.getFlightDetailsUseCase = getFlightDetailsUseCase;
        this.getSeatMapUseCase = getSeatMapUseCase;
    }

    @Operation(
            summary = "Flight search",
            description = "Allows you to find available flights by airport codes and departure date"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Flights successfully found"
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid query parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "Token is missing or invalid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "Access denied",
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
    @ErrorApiResponse(status = HttpStatus.BAD_REQUEST, errorCode = ApiErrorCode.VALIDATION_ERROR, message = "date : Departure date must be today or in the future")
    @ErrorApiResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ApiErrorCode.TOKEN_INVALID, message = "JWT token is invalid")
    @ErrorApiResponse(status = HttpStatus.FORBIDDEN, errorCode = ApiErrorCode.ACCESS_DENIED, message = "Access denied")
    @ErrorApiResponse(status = HttpStatus.NOT_FOUND, errorCode = ApiErrorCode.ENTITY_NOT_FOUND, message = "Airport with code KBA not found")
    @GetMapping("/search")
    public ResponseEntity<PageResponse<FlightSearchResponse>> searchFlights(
            @ParameterObject @Valid @ModelAttribute FlightSearchRequest request,
            @ParameterObject Pageable pageable
    ) {
        PageResponse<FlightSearchResponse> flights = searchFlightsUseCase.execute(request, pageable);
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
                    responseCode = "401", description = "Token is missing or invalid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "Access denied",
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
    @ErrorApiResponse(status = HttpStatus.BAD_REQUEST, errorCode = ApiErrorCode.VALIDATION_ERROR, message = "id : Flight id must be greater than zero")
    @ErrorApiResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ApiErrorCode.TOKEN_INVALID, message = "JWT token is invalid")
    @ErrorApiResponse(status = HttpStatus.FORBIDDEN, errorCode = ApiErrorCode.ACCESS_DENIED, message = "Access denied")
    @ErrorApiResponse(status = HttpStatus.NOT_FOUND, errorCode = ApiErrorCode.ENTITY_NOT_FOUND, message = "Flight with id 1000 was not found")
    @GetMapping("/{id}")
    public ResponseEntity<FlightSearchResponse> getFlightDetails(
            @PathVariable
            @Parameter(example = "5", required = true)
            @Positive(message = "Flight id must be greater than zero")
            Long id
    ) {
        FlightSearchResponse flight = getFlightDetailsUseCase.execute(id);
        return ResponseEntity.ok(flight);
    }

    @Operation(
            summary = "Get seat map",
            description = "Provides information about seat assignments and seat availability"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Information about seats successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SeatMapResponse.class)
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
                    responseCode = "401", description = "Token is missing or invalid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "Access denied",
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
    @ErrorApiResponse(status = HttpStatus.BAD_REQUEST, errorCode = ApiErrorCode.VALIDATION_ERROR, message = "id : Flight id must be greater than zero")
    @ErrorApiResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ApiErrorCode.TOKEN_INVALID, message = "JWT token is invalid")
    @ErrorApiResponse(status = HttpStatus.FORBIDDEN, errorCode = ApiErrorCode.ACCESS_DENIED, message = "Access denied")
    @ErrorApiResponse(status = HttpStatus.NOT_FOUND, errorCode = ApiErrorCode.ENTITY_NOT_FOUND, message = "Flight with id 1000 was not found")
    @GetMapping("/{id}/seats")
    public ResponseEntity<SeatMapResponse> getSeatMap(
            @PathVariable
            @Parameter(example = "5", required = true)
            @Positive(message = "Flight id must be greater than zero")
            Long id
    ) {
        return ResponseEntity.ok(getSeatMapUseCase.execute(id));
    }
}
