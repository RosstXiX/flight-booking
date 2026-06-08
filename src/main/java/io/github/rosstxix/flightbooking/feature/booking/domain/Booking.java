package io.github.rosstxix.flightbooking.feature.booking.domain;

import io.github.rosstxix.flightbooking.common.domain.Auditable;
import io.github.rosstxix.flightbooking.feature.user.domain.User;
import io.github.rosstxix.flightbooking.feature.flight.domain.Flight;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.InvalidBookingStateApiException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "bookings")
@Getter
@NoArgsConstructor
public class Booking extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;  // "17B", "3C" etc

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    public Booking(User user, Flight flight, String seatNumber) {
        this.user = user;
        this.flight = flight;
        this.seatNumber = seatNumber;
    }

    public void confirmBooking() {
        if (this.status != BookingStatus.PENDING) {
            throw new InvalidBookingStateApiException("Booking with id %d is not in pending state".formatted(this.id));
        }
        this.status = BookingStatus.CONFIRMED;
    }
}
