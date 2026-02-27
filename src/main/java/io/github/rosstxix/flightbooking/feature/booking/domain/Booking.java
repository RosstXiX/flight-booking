package io.github.rosstxix.flightbooking.feature.booking.domain;

import io.github.rosstxix.flightbooking.domain.Auditable;
import io.github.rosstxix.flightbooking.domain.User;
import io.github.rosstxix.flightbooking.feature.flight.domain.Flight;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;


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

    @Column(name = "booking_date", nullable = false)
    private Instant bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    public Booking(User user, Flight flight, String seatNumber, Instant bookingDate) {
        this.user = user;
        this.flight = flight;
        this.seatNumber = seatNumber;
        this.bookingDate = bookingDate;
    }
}
