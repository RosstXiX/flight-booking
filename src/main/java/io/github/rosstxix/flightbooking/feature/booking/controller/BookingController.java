package io.github.rosstxix.flightbooking.feature.booking.controller;

import io.github.rosstxix.flightbooking.feature.booking.dto.request.BookingCreateRequest;
import io.github.rosstxix.flightbooking.feature.booking.usecase.CreateBookingUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final CreateBookingUseCase createBookingUseCase;

    public BookingController(CreateBookingUseCase createBookingUseCase) {
        this.createBookingUseCase = createBookingUseCase;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createBooking(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody BookingCreateRequest request
    ) {
        createBookingUseCase.execute(
                (Long) jwt.getClaims().get("userId"),
                request.flightId(),
                request.seatNumber()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
