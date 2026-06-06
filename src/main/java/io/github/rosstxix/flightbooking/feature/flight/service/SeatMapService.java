package io.github.rosstxix.flightbooking.feature.flight.service;

import io.github.rosstxix.flightbooking.feature.booking.repository.BookingRepository;
import io.github.rosstxix.flightbooking.feature.flight.dto.local.SeatDTO;
import io.github.rosstxix.flightbooking.feature.flight.dto.local.SeatRowDTO;
import io.github.rosstxix.flightbooking.feature.flight.dto.projection.SeatMapInfoProjection;
import io.github.rosstxix.flightbooking.feature.flight.dto.response.SeatMapResponse;
import io.github.rosstxix.flightbooking.feature.flight.repository.FlightRepository;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SeatMapService {

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    public SeatMapService(
            FlightRepository flightRepository,
            BookingRepository bookingRepository
    ) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
    }

    public SeatMapResponse getSeatMap(Long id) {
        SeatMapInfoProjection projection = flightRepository.findSeatMapInfoProjection(id).orElseThrow(
                () -> new EntityNotFoundApiException("Flight with id %d not found".formatted(id))
        );
        int rows = projection.getRows();
        int seatPerRow = projection.getSeatPerRow();
        String seatLayout = projection.getSeatLayout().replace("_", "");

        int premiumRows = projection.getPremiumRows();
        int premiumSeatPerRow = projection.getSeatPerPremiumRow();
        String premiumSeatLayout = projection.getPremiumSeatLayout().replace("_", "");

        Set<String> occupiedSeats = bookingRepository.findOccupiedSeatNumbersByFlightId(id);

        List<SeatRowDTO> seatRows = new ArrayList<>();
        proceedSeatRows(seatRows, premiumRows, 0, premiumSeatPerRow, premiumSeatLayout, occupiedSeats, true);
        proceedSeatRows(seatRows, rows, premiumRows, seatPerRow, seatLayout, occupiedSeats, false);

        return new SeatMapResponse(
                projection.getAircraftModel(),
                projection.getTotalSeats(),
                projection.getSeatLayout(),
                projection.getPremiumSeatLayout(),
                seatRows
        );

    }

    private void proceedSeatRows(
            List<SeatRowDTO> seatRows,
            int rows,
            int startRow,
            int seatPerRow,
            String seatLayout,
            Set<String> occupiedSeats,
            boolean isPremium
    ) {

        for (int i = startRow; i < rows + startRow; i++) {
            List<SeatDTO> seats = new ArrayList<>();
            for (int j = 0; j < seatPerRow; j++) {
                String seatNumber = (i + 1) + String.valueOf(seatLayout.charAt(j));
                seats.add(new SeatDTO(
                        seatNumber,
                        occupiedSeats.contains(seatNumber)
                ));
            }
            seatRows.add(new SeatRowDTO(
                    i + 1,
                    isPremium,
                    seats
            ));
        }
    }

}
