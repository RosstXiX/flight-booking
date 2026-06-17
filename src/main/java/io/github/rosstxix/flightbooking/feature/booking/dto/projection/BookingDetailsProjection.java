package io.github.rosstxix.flightbooking.feature.booking.dto.projection;

import io.github.rosstxix.flightbooking.feature.booking.domain.BookingStatus;
import io.github.rosstxix.flightbooking.feature.booking.payment.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public interface BookingDetailsProjection {
    // Booking
    Long getId();
    BookingStatus getStatus();
    String getSeatNumber();
    Instant getCreatedAt();

    // Flight
    Long getFlightId();
    String getFlightNumber();
    Instant getDepartureUtc();
    Instant getArrivalUtc();
    String getAircraftModel();

    // Departure airport
    String getDepartureAirportCode();
    String getDepartureAirportCity();
    String getDepartureAirportCountry();

    // Arrival airport
    String getArrivalAirportCode();
    String getArrivalAirportCity();
    String getArrivalAirportCountry();

    // Payment
    BigDecimal getPaymentAmount();
    String getPaymentCurrency();
    PaymentStatus getPaymentStatus();
}
