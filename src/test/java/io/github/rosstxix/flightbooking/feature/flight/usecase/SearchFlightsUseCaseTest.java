package io.github.rosstxix.flightbooking.feature.flight.usecase;

import io.github.rosstxix.flightbooking.common.dto.PageResponse;
import io.github.rosstxix.flightbooking.feature.catalog.airport.service.AirportService;
import io.github.rosstxix.flightbooking.feature.flight.domain.FlightStatus;
import io.github.rosstxix.flightbooking.feature.flight.dto.projection.FlightProjection;
import io.github.rosstxix.flightbooking.feature.flight.dto.request.FlightSearchRequest;
import io.github.rosstxix.flightbooking.feature.flight.dto.response.FlightSearchResponse;
import io.github.rosstxix.flightbooking.feature.flight.mapper.FlightMapper;
import io.github.rosstxix.flightbooking.feature.flight.repository.FlightRepository;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SearchFlightsUseCaseTest {

    private static final String FROM_CODE = "KBP";
    private static final String TO_CODE = "LWO";
    private static final LocalDate SEARCH_DATE = LocalDate.of(2026, 10, 20);

    @Mock
    private FlightRepository flightRepository;
    @Mock
    private FlightMapper flightMapper;
    @Mock
    private AirportService airportService;
    @InjectMocks
    private SearchFlightsUseCase searchFlightsUseCase;

    @Test
    void execute_shouldReturnPageResponse_whenFlightsFound() {
        // Arrange
        FlightSearchRequest request = buildSearchRequest();
        Pageable pageable = PageRequest.of(0, 10);
        FlightProjection projection = mock(FlightProjection.class);
        long totalElements = 1L;
        Page<FlightProjection> projections = new PageImpl<>(
                List.of(projection),
                pageable,
                totalElements
        );
        FlightSearchResponse mappedResponse = buildFlightSearchResponse();

        when(airportService.getTimeZoneByCode(FROM_CODE))
                .thenReturn("Europe/Kyiv");

        when(flightRepository.searchFlights(any(), any(), any(), any(), any(), any()))
                .thenReturn(projections);

        when(flightMapper.toSearchResponse(projection))
                .thenReturn(mappedResponse);

        // Act
        PageResponse<FlightSearchResponse> response = searchFlightsUseCase.execute(request, pageable);

        // Assert
        assertThat(response.content()).hasSize(1);
        assertThat(response.content().get(0)).isSameAs(mappedResponse);

        assertThat(response.page()).isEqualTo(pageable.getPageNumber());
        assertThat(response.size()).isEqualTo(pageable.getPageSize());
        assertThat(response.totalElements()).isEqualTo(totalElements);
        assertThat(response.first()).isTrue();
        assertThat(response.last()).isTrue();

        verify(flightMapper).toSearchResponse(projection);
        verify(flightRepository).searchFlights(
                eq(FROM_CODE),
                eq(TO_CODE),
                any(Instant.class),
                any(Instant.class),
                eq(FlightStatus.SCHEDULED),
                eq(pageable)
        );

    }

    @Test
    void execute_shouldReturnEmptyPageResponse_whenNoFlightsFound() {
        // Arrange
        FlightSearchRequest request = buildSearchRequest();
        Pageable pageable = PageRequest.of(0, 10);

        when(airportService.getTimeZoneByCode(FROM_CODE))
                .thenReturn("Europe/Kyiv");
        when(flightRepository.searchFlights(any(), any(), any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(), pageable, 0L));

        // Act
        PageResponse<FlightSearchResponse> response = searchFlightsUseCase.execute(request, pageable);

        // Assert
        assertThat(response.content()).isEmpty();
        assertThat(response.page()).isEqualTo(pageable.getPageNumber());
        assertThat(response.size()).isEqualTo(pageable.getPageSize());
        assertThat(response.totalElements()).isEqualTo(0L);
        assertThat(response.first()).isTrue();
        assertThat(response.last()).isTrue();
    }

    @Test
    void execute_shouldConvertLocalDateToCorrectUtcRange_whenAirportTimezoneIsKyiv() {
        // Arrange
        FlightSearchRequest request = buildSearchRequest();
        Pageable pageable = PageRequest.of(0, 10);

        when(airportService.getTimeZoneByCode(FROM_CODE))
                .thenReturn("Europe/Kyiv");

        when(flightRepository.searchFlights(any(), any(), any(), any(), any(), any()))
                .thenReturn(Page.empty());

        // Act
        searchFlightsUseCase.execute(request, pageable);

        // Assert
        ArgumentCaptor<Instant> startCaptor = ArgumentCaptor.forClass(Instant.class);
        ArgumentCaptor<Instant> endCaptor = ArgumentCaptor.forClass(Instant.class);

        verify(flightRepository).searchFlights(
                eq(FROM_CODE),
                eq(TO_CODE),
                startCaptor.capture(),
                endCaptor.capture(),
                eq(FlightStatus.SCHEDULED),
                eq(pageable)
        );

        ZoneId kyiv = ZoneId.of("Europe/Kyiv");

        Instant expectedStart = SEARCH_DATE.atStartOfDay(kyiv).toInstant();
        Instant expectedEnd = SEARCH_DATE.plusDays(1).atStartOfDay(kyiv).toInstant();

        assertThat(startCaptor.getValue()).isEqualTo(expectedStart);
        assertThat(endCaptor.getValue()).isEqualTo(expectedEnd);
    }

    @Test
    void execute_shouldConvertLocalDateToCorrectUtcRange_whenAirportTimezoneIsUtc() {
        // Arrange
        FlightSearchRequest request = buildSearchRequest();
        Pageable pageable = PageRequest.of(0, 10);

        when(airportService.getTimeZoneByCode(FROM_CODE))
                .thenReturn("UTC");

        when(flightRepository.searchFlights(any(), any(), any(), any(), any(), any()))
                .thenReturn(Page.empty());

        // Act
        searchFlightsUseCase.execute(request, pageable);

        // Assert
        ArgumentCaptor<Instant> startCaptor = ArgumentCaptor.forClass(Instant.class);
        ArgumentCaptor<Instant> endCaptor = ArgumentCaptor.forClass(Instant.class);

        verify(flightRepository).searchFlights(
                eq(FROM_CODE),
                eq(TO_CODE),
                startCaptor.capture(),
                endCaptor.capture(),
                eq(FlightStatus.SCHEDULED),
                eq(pageable)
        );

        ZoneId Utc = ZoneId.of("UTC");

        Instant expectedStart = SEARCH_DATE.atStartOfDay(Utc).toInstant();
        Instant expectedEnd = SEARCH_DATE.plusDays(1).atStartOfDay(Utc).toInstant();

        assertThat(startCaptor.getValue()).isEqualTo(expectedStart);
        assertThat(endCaptor.getValue()).isEqualTo(expectedEnd);
    }

    @Test
    void execute_shouldThrowEntityNotFoundApiException_whenDepartureAirportNotFound() {
        // Arrange
        FlightSearchRequest request = buildSearchRequest();
        EntityNotFoundApiException exception = new EntityNotFoundApiException("Airport not found");
        when(airportService.getTimeZoneByCode(FROM_CODE)).thenThrow(exception);

        // Act & Assert
        assertThatThrownBy(() -> searchFlightsUseCase.execute(request, PageRequest.of(0, 10)))
                .isSameAs(exception);

        verifyNoInteractions(flightRepository, flightMapper);

    }

    private FlightSearchRequest buildSearchRequest() {
        return new FlightSearchRequest(FROM_CODE, TO_CODE, SEARCH_DATE);
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
