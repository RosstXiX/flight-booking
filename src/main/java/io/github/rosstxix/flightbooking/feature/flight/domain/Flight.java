package io.github.rosstxix.flightbooking.feature.flight.domain;

import io.github.rosstxix.flightbooking.feature.catalog.aircraft.domain.Aircraft;
import io.github.rosstxix.flightbooking.feature.catalog.airport.domain.Airport;
import io.github.rosstxix.flightbooking.common.domain.Auditable;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(name = "flights")
@Getter
@NoArgsConstructor
public class Flight extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;    // "A1524", "GH154" etc

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_airport_id", nullable = false)
    private Airport departureAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_airport_id", nullable = false)
    private Airport arrivalAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_id", nullable = false)
    private Aircraft aircraft;

    @Column(name = "departure_utc", nullable = false)
    private Instant departureUtc;

    @Column(name = "arrival_utc", nullable = false)
    private Instant arrivalUtc;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlightStatus status = FlightStatus.SCHEDULED;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @Column(nullable = false, length = 3)
    private String currency;


    public Flight(String flightNumber, Airport departureAirport, Airport arrivalAirport, Aircraft aircraft,
                  Instant departureUtc, Instant arrivalUtc, BigDecimal price, Integer availableSeats, String currency) {
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.aircraft = aircraft;
        this.departureUtc = departureUtc;
        this.arrivalUtc = arrivalUtc;
        this.price = price;
        this.availableSeats = availableSeats;
        this.currency = currency;
    }

    public void decrementAvailableSeats() {
        if (availableSeats <= 0) {
            throw new NoSeatsAvailableException("Flight %d has no available seats".formatted(this.id));
        }
        this.availableSeats--;
    }

    public void incrementAvailableSeats() {
        this.availableSeats++;
    }
}
