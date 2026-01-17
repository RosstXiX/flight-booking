package io.github.rosstxix.flightbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FlightSearchRequest {
    private String fromCode;
    private String toCode;
    private LocalDate date;

}
