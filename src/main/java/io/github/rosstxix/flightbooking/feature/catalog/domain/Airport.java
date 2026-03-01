package io.github.rosstxix.flightbooking.feature.catalog.domain;

import io.github.rosstxix.flightbooking.common.domain.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "airports")
@Getter
@NoArgsConstructor
public class Airport extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 3)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(name = "timezone", nullable = false)
    private String timeZone;

    public Airport(String code, String name, String city, String country, String timeZone) {
        this.code = code;
        this.name = name;
        this.city = city;
        this.country = country;
        this.timeZone = timeZone;
    }
}
