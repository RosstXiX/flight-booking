package io.github.rosstxix.flightbooking.feature.flight.service;

import io.github.rosstxix.flightbooking.common.dto.PageResponse;
import io.github.rosstxix.flightbooking.feature.catalog.airport.domain.Airport;
import io.github.rosstxix.flightbooking.feature.catalog.airport.repository.AirportRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    private static final String FROM_CODE = "KBP";
    private static final String TO_CODE = "LWO";
    private static final LocalDate SEARCH_DATE = LocalDate.of(2026, 10, 20);

    @Mock
    private FlightRepository flightRepository;
    @Mock
    private FlightMapper flightMapper;
    @Mock
    private AirportRepository airportRepository;
    @InjectMocks
    private FlightService flightService;

    // -------------------
    // searchFlights
    // -------------------

    @Test
    void searchFlights_shouldReturnPageResponse_whenFlightsFound() {
        // Arrange
        FlightSearchRequest request = buildSearchRequest();
        Pageable pageable = PageRequest.of(0, 10);
        Airport departureAirport = buildDepartureAirport();
        FlightProjection projection = mock(FlightProjection.class);
        long totalElements = 1L;
        Page<FlightProjection> projections = new PageImpl<>(
                List.of(projection),
                pageable,
                totalElements
        );
        FlightSearchResponse mappedResponse = buildFlightSearchResponse();

        when(airportRepository.findByCode(FROM_CODE))
                .thenReturn(Optional.of(departureAirport));

        when(flightRepository.searchFlights(any(), any(), any(), any(), any(), any()))
                .thenReturn(projections);

        when(flightMapper.toSearchResponse(projection))
                .thenReturn(mappedResponse);

        // Act
        PageResponse<FlightSearchResponse> response = flightService.searchFlights(request, pageable);

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
    void searchFlights_shouldReturnEmptyPageResponse_whenNoFlightsFound() {
        // Arrange
        FlightSearchRequest request = buildSearchRequest();
        Pageable pageable = PageRequest.of(0, 10);
        Airport departureAirport = buildDepartureAirport();

        when(airportRepository.findByCode(FROM_CODE))
                .thenReturn(Optional.of(departureAirport));
        when(flightRepository.searchFlights(any(), any(), any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(), pageable, 0L));

        // Act
        PageResponse<FlightSearchResponse> response = flightService.searchFlights(request, pageable);

        // Assert
        assertThat(response.content()).isEmpty();
        assertThat(response.page()).isEqualTo(pageable.getPageNumber());
        assertThat(response.size()).isEqualTo(pageable.getPageSize());
        assertThat(response.totalElements()).isEqualTo(0L);
        assertThat(response.first()).isTrue();
        assertThat(response.last()).isTrue();
    }

    @Test
    void searchFlights_shouldConvertLocalDateToCorrectUtcRange_whenAirportTimezoneIsKyiv() {
        // Arrange
        Airport departureAirport = buildDepartureAirport("Europe/Kyiv");
        FlightSearchRequest request = buildSearchRequest();
        Pageable pageable = PageRequest.of(0, 10);

        when(airportRepository.findByCode(FROM_CODE))
                .thenReturn(Optional.of(departureAirport));

        when(flightRepository.searchFlights(any(), any(), any(), any(), any(), any()))
                .thenReturn(Page.empty());

        // Act
        flightService.searchFlights(request, pageable);

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

        ZoneId kyiv = ZoneId.of(departureAirport.getTimeZone());

        Instant expectedStart = SEARCH_DATE.atStartOfDay(kyiv).toInstant();
        Instant expectedEnd = SEARCH_DATE.plusDays(1).atStartOfDay(kyiv).toInstant();

        assertThat(startCaptor.getValue()).isEqualTo(expectedStart);
        assertThat(endCaptor.getValue()).isEqualTo(expectedEnd);
    }

    @Test
    void searchFlights_shouldConvertLocalDateToCorrectUtcRange_whenAirportTimezoneIsUtc() {
        // Arrange
        Airport departureAirport = buildDepartureAirport("UTC");
        FlightSearchRequest request = buildSearchRequest();
        Pageable pageable = PageRequest.of(0, 10);

        when(airportRepository.findByCode(FROM_CODE))
                .thenReturn(Optional.of(departureAirport));

        when(flightRepository.searchFlights(any(), any(), any(), any(), any(), any()))
                .thenReturn(Page.empty());

        // Act
        flightService.searchFlights(request, pageable);

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

        ZoneId Utc = ZoneId.of(departureAirport.getTimeZone());

        Instant expectedStart = SEARCH_DATE.atStartOfDay(Utc).toInstant();
        Instant expectedEnd = SEARCH_DATE.plusDays(1).atStartOfDay(Utc).toInstant();

        assertThat(startCaptor.getValue()).isEqualTo(expectedStart);
        assertThat(endCaptor.getValue()).isEqualTo(expectedEnd);
    }

    @Test
    void searchFlights_shouldThrowEntityNotFoundApiException_whenDepartureAirportNotFound() {
        // Arrange
        FlightSearchRequest request = buildSearchRequest();
        when(airportRepository.findByCode(FROM_CODE)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> flightService.searchFlights(request, PageRequest.of(0, 10)))
                .isInstanceOf(EntityNotFoundApiException.class)
                .hasMessageContaining(FROM_CODE);

        verifyNoInteractions(flightRepository, flightMapper);

    }

    // -------------------
    // getFlightDetails
    // -------------------

    @Test
    void getFlightDetails_shouldReturnFlightResponse_whenFlightFound() {
        // Arrange
        long id = 1L;
        FlightProjection projection = mock(FlightProjection.class);
        FlightSearchResponse mappedResponse = buildFlightSearchResponse();

        when(flightRepository.findProjectionById(id))
                .thenReturn(Optional.of(projection));
        when(flightMapper.toSearchResponse(projection))
                .thenReturn(mappedResponse);

        // Act
        FlightSearchResponse response = flightService.getFlightDetails(id);

        // Assert
        assertThat(response).isSameAs(mappedResponse);

        verify(flightMapper).toSearchResponse(projection);

    }

    @Test
    void getFlightDetails_shouldThrowEntityNotFoundApiException_whenFlightNotFound() {
        // Arrange
        long id = 1L;
        when(flightRepository.findProjectionById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> flightService.getFlightDetails(id))
                .isInstanceOf(EntityNotFoundApiException.class)
                .hasMessageContaining(String.valueOf(id));

        verifyNoInteractions(flightMapper);
    }


    private Airport buildDepartureAirport() {
        return new Airport(
                FROM_CODE, "Boryspil International Airport", "Kyiv", "Ukraine", "Europe/Kyiv"
        );
    }

    private Airport buildDepartureAirport(String timezone) {
        return new Airport(
                FROM_CODE, "Boryspil International Airport", "Kyiv", "Ukraine", timezone
        );
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
