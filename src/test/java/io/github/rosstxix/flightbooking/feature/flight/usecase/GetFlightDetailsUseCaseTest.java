package io.github.rosstxix.flightbooking.feature.flight.usecase;

import io.github.rosstxix.flightbooking.feature.flight.dto.projection.FlightProjection;
import io.github.rosstxix.flightbooking.feature.flight.dto.response.FlightSearchResponse;
import io.github.rosstxix.flightbooking.feature.flight.mapper.FlightMapper;
import io.github.rosstxix.flightbooking.feature.flight.repository.FlightRepository;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetFlightDetailsUseCaseTest {

    @Mock
    private FlightRepository flightRepository;
    @Mock
    private FlightMapper flightMapper;
    @InjectMocks
    private GetFlightDetailsUseCase getFlightDetailsUseCase;

    @Test
    void execute_shouldReturnFlightResponse_whenFlightFound() {
        // Arrange
        long id = 1L;
        FlightProjection projection = mock(FlightProjection.class);
        FlightSearchResponse mappedResponse = buildFlightSearchResponse();

        when(flightRepository.findProjectionById(id))
                .thenReturn(Optional.of(projection));
        when(flightMapper.toSearchResponse(projection))
                .thenReturn(mappedResponse);

        // Act
        FlightSearchResponse response = getFlightDetailsUseCase.execute(id);

        // Assert
        assertThat(response).isSameAs(mappedResponse);

        verify(flightMapper).toSearchResponse(projection);

    }

    @Test
    void execute_shouldThrowEntityNotFoundApiException_whenFlightNotFound() {
        // Arrange
        long id = 1L;
        when(flightRepository.findProjectionById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> getFlightDetailsUseCase.execute(id))
                .isInstanceOf(EntityNotFoundApiException.class)
                .hasMessageContaining(String.valueOf(id));

        verifyNoInteractions(flightMapper);
    }

    private FlightSearchResponse buildFlightSearchResponse() {
        return new FlightSearchResponse(
                1L, "PS101",
                "KBP", "Kyiv",
                "LWO", "Lviv",
                ZonedDateTime.of(LocalDateTime.of(2026, 10, 20, 8, 0), ZoneId.of("Europe/Kyiv")),
                ZonedDateTime.of(LocalDateTime.of(2026, 10, 20, 9, 15), ZoneId.of("Europe/Kyiv")),
                "Boeing 737-800", 189, 189,
                new BigDecimal("1500.00"), "USD", "SCHEDULED", 75L
        );
    }
}
