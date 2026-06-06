package io.github.rosstxix.flightbooking.feature.catalog.domain;

import io.github.rosstxix.flightbooking.common.domain.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "aircrafts")
@Getter
@NoArgsConstructor
public class Aircraft extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String model;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(name = "seat_layout", nullable = false)
    private String seatLayout;

    @Column(name = "seat_per_row", nullable = false)
    private Integer seatPerRow;

    @Column(nullable = false)
    private Integer rows;

    @Column(name = "premium_seat_layout", nullable = false)
    private String premiumSeatLayout;

    @Column(name = "premium_rows", nullable = false)
    private Integer premiumRows;

    @Column(name = "seat_per_premium_row", nullable = false)
    private Integer seatPerPremiumRow;

    public Aircraft(
            String model,
            Integer totalSeats,
            String seatLayout,
            Integer seatPerRow,
            Integer rows,
            String premiumSeatLayout,
            Integer premiumRows,
            Integer seatPerPremiumRow
    ) {
        this.model = model;
        this.totalSeats = totalSeats;
        this.seatLayout = seatLayout;
        this.seatPerRow = seatPerRow;
        this.rows = rows;
        this.premiumSeatLayout = premiumSeatLayout;
        this.premiumRows = premiumRows;
        this.seatPerPremiumRow = seatPerPremiumRow;
    }
}
