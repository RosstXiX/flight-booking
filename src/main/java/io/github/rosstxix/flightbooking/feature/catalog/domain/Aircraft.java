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

    public Aircraft(String model, Integer totalSeats) {
        this.model = model;
        this.totalSeats = totalSeats;
    }
}
