package io.github.rosstxix.flightbooking.feature.booking.controller;

import io.github.rosstxix.flightbooking.feature.booking.dto.request.BookingCreateRequest;
import io.github.rosstxix.flightbooking.feature.booking.dto.response.BookingDetailsResponse;
import io.github.rosstxix.flightbooking.feature.booking.usecase.CancelBookingUseCase;
import io.github.rosstxix.flightbooking.feature.booking.usecase.CreateBookingUseCase;
import io.github.rosstxix.flightbooking.feature.booking.usecase.GetBookingDetailsUseCase;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ErrorResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Tag(name = "Bookings", description = "Booking management")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final CreateBookingUseCase createBookingUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;
    private final GetBookingDetailsUseCase getBookingDetailsUseCase;

    public BookingController(
            CreateBookingUseCase createBookingUseCase,
            CancelBookingUseCase cancelBookingUseCase,
            GetBookingDetailsUseCase getBookingDetailsUseCase
    ) {
        this.createBookingUseCase = createBookingUseCase;
        this.cancelBookingUseCase = cancelBookingUseCase;
        this.getBookingDetailsUseCase = getBookingDetailsUseCase;
    }

    @Operation(
            summary = "Booking creation",
            description = "Allows you to book a seat for flight"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "Booking created successfully"
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid request body",
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
                    responseCode = "404", description = "Flight you trying to book for was not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "Seat you trying to book already booked",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422", description = "Seat you trying to book does not exist",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @ErrorApiResponse(status = HttpStatus.BAD_REQUEST, errorCode = ApiErrorCode.VALIDATION_ERROR, message = "seatNumber : Seat number must be in the format '1A', '12B', etc")
    @ErrorApiResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ApiErrorCode.TOKEN_INVALID, message = "JWT token is invalid")
    @ErrorApiResponse(status = HttpStatus.FORBIDDEN, errorCode = ApiErrorCode.ACCESS_DENIED, message = "Access denied")
    @ErrorApiResponse(status = HttpStatus.NOT_FOUND, errorCode = ApiErrorCode.ENTITY_NOT_FOUND, message = "Flight with id 1000 was not found")
    @ErrorApiResponse(status = HttpStatus.CONFLICT, errorCode = ApiErrorCode.SEAT_ALREADY_BOOKED, message = "Seat with number 1A already booked")
    @ErrorApiResponse(status = HttpStatus.UNPROCESSABLE_ENTITY, errorCode = ApiErrorCode.SEAT_DOES_NOT_EXIST, message = "Seat with number 99R does not exist")
    @PostMapping("/create")
    public ResponseEntity<Void> createBooking(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody BookingCreateRequest request
    ) {
        createBookingUseCase.execute(
                (Long) jwt.getClaims().get("userId"),
                request.flightId(),
                request.seatNumber()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDetailsResponse> getBookingDetails(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable
            @Positive(message = "Booking id must be greater than zero")
            Long id
    ) {
        Long userId = (Long) jwt.getClaims().get("userId");
        BookingDetailsResponse response = getBookingDetailsUseCase.execute(userId, id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Booking cancellation",
            description = "Allows you to cancel your booking and refund your money"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Booking cancelled successfully"
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid request body",
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
                    responseCode = "404", description = "Booking you trying to cancel was not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "Booking you trying to cancel is already cancelled",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @ErrorApiResponse(status = HttpStatus.BAD_REQUEST, errorCode = ApiErrorCode.VALIDATION_ERROR, message = "id : Booking id must be greater than zero")
    @ErrorApiResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ApiErrorCode.TOKEN_INVALID, message = "JWT token is invalid")
    @ErrorApiResponse(status = HttpStatus.FORBIDDEN, errorCode = ApiErrorCode.ACCESS_DENIED, message = "Access denied")
    @ErrorApiResponse(status = HttpStatus.NOT_FOUND, errorCode = ApiErrorCode.ENTITY_NOT_FOUND, message = "Booking with id 1000 was not found")
    @ErrorApiResponse(status = HttpStatus.CONFLICT, errorCode = ApiErrorCode.INVALID_BOOKING_STATE, message = "Booking with id 5 is already cancelled")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable
            @Parameter(example = "5", required = true)
            @Positive(message = "Booking id must be greater than zero")
            Long id
    ) {
        cancelBookingUseCase.execute(
                (Long) jwt.getClaims().get("userId"),
                id
        );
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
