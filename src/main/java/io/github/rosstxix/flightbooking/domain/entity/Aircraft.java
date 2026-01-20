package io.github.rosstxix.flightbooking.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "aircrafts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String model;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @OneToMany(mappedBy = "aircraft")
    private List<Flight> flights;
}
