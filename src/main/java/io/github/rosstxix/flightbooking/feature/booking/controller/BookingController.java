package io.github.rosstxix.flightbooking.feature.booking.controller;

import io.github.rosstxix.flightbooking.feature.booking.dto.request.BookingCreateRequest;
import io.github.rosstxix.flightbooking.feature.booking.service.BookingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public void createBooking(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody BookingCreateRequest request
    ) {
        bookingService.createBooking(
                (Long) jwt.getClaims().get("userId"),
                request.flightId(),
                request.seatNumber()
        );
    }
}
