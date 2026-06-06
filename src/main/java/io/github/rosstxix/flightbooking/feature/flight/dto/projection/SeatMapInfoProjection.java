package io.github.rosstxix.flightbooking.feature.flight.dto.projection;

public interface SeatMapInfoProjection {
    // Aircraft entity
    String getAircraftModel();
    Integer getTotalSeats();

    String getSeatLayout();
    Integer getRows();
    Integer getSeatPerRow();

    String getPremiumSeatLayout();
    Integer getPremiumRows();
    Integer getSeatPerPremiumRow();
}
