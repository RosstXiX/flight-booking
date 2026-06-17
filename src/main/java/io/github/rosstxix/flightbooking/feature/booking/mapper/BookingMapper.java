package io.github.rosstxix.flightbooking.feature.booking.mapper;

import io.github.rosstxix.flightbooking.feature.booking.dto.projection.BookingDetailsProjection;
import io.github.rosstxix.flightbooking.feature.booking.dto.response.BookingDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "status", expression = "java(projection.getStatus().name())")
    @Mapping(target = "flight", expression = "java(mapFlight(projection))")
    @Mapping(target = "payment", expression = "java(mapPayment(projection))")
    BookingDetailsResponse toDetailsResponse(BookingDetailsProjection projection);

    @Mapping(target = "id", source = "flightId")
    @Mapping(target = "departure", expression = "java(mapDeparture(projection))")
    @Mapping(target = "arrival", expression = "java(mapArrival(projection))")
    BookingDetailsResponse.FlightInfo mapFlight(BookingDetailsProjection projection);

    @Mapping(target = "status", expression = "java(projection.getPaymentStatus().name())")
    BookingDetailsResponse.PaymentInfo mapPayment(BookingDetailsProjection projection);

    default BookingDetailsResponse.AirportInfo mapDeparture(BookingDetailsProjection projection) {
        return new BookingDetailsResponse.AirportInfo(
                projection.getDepartureAirportCode(),
                projection.getDepartureAirportCity(),
                projection.getDepartureAirportCountry()
        );
    }

    default BookingDetailsResponse.AirportInfo mapArrival(BookingDetailsProjection projection) {
        return new BookingDetailsResponse.AirportInfo(
                projection.getArrivalAirportCode(),
                projection.getArrivalAirportCity(),
                projection.getArrivalAirportCountry()
        );
    }
}
