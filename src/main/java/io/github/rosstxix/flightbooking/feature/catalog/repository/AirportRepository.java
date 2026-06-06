package io.github.rosstxix.flightbooking.feature.catalog.repository;

import io.github.rosstxix.flightbooking.feature.catalog.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    Optional<Airport> findByCode(String code);

    @Query("""
            SELECT a.timeZone
            FROM Airport a
            WHERE a.code = :code
            """)
    Optional<String> findTimeZoneByCode(
            @Param("code") String code
    );
}
