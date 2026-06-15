package io.github.rosstxix.flightbooking.feature.catalog.aircraft.domain;

import io.github.rosstxix.flightbooking.infrastructure.error.exception.SeatDoesNotExistApiException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AircraftTest {

    Aircraft aircraft = new Aircraft(
            "Model", 102,
            "ABC_EDF", 6, 15,
            "AB_CD", 4, 3
    );


    @Test
    void validateSeat_shouldPass_whenValidSeat() {
        // Arrange
        String seatNumber = "7A";

        // Act and Assert
        assertThatNoException().isThrownBy(() -> aircraft.validateSeat(seatNumber));
    }

    @Test
    void validateSeat_shouldPass_whenValidPremiumSeat() {
        // Arrange
        String seatNumber = "19B";

        // Act and Assert
        assertThatNoException().isThrownBy(() -> aircraft.validateSeat(seatNumber));
    }

    @Test
    void validateSeat_shouldThrowSeatDoesNotExistApiException_whenSeatRowLessThan1() {
        // Arrange
        String seatNumber = "0A";

        // Act and Assert
        assertThatThrownBy(() -> aircraft.validateSeat(seatNumber))
                .isInstanceOf(SeatDoesNotExistApiException.class)
                .hasMessageContaining(seatNumber);
    }

    @Test
    void validateSeat_shouldThrowSeatDoesNotExistApiException_whenSeatRowExceedsMaxRows() {
        // Arrange
        String seatNumber = "99A";

        // Act and Assert
        assertThatThrownBy(() -> aircraft.validateSeat(seatNumber))
                .isInstanceOf(SeatDoesNotExistApiException.class)
                .hasMessageContaining(seatNumber);
    }

    @Test
    void validateSeat_shouldThrowSeatDoesNotExistApiException_whenSeatLetterNotInPremiumSeatLayout() {
        // Arrange
        String seatNumber = "1E";

        // Act and Assert
        assertThatThrownBy(() -> aircraft.validateSeat(seatNumber))
                .isInstanceOf(SeatDoesNotExistApiException.class)
                .hasMessageContaining(seatNumber);
    }

    @Test
    void validateSeat_shouldThrowSeatDoesNotExistApiException_whenSeatLetterNotInSeatLayout() {
        // Arrange
        String seatNumber = "7H";

        // Act and Assert
        assertThatThrownBy(() -> aircraft.validateSeat(seatNumber))
                .isInstanceOf(SeatDoesNotExistApiException.class)
                .hasMessageContaining(seatNumber);
    }

}
