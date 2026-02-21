package io.github.rosstxix.flightbooking.feature.flight.dto.response;

import io.github.rosstxix.flightbooking.dto.PageResponseDoc;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        name = "FlightSearchPage",
        description = "Page of flight search results"
)
public final class FlightSearchPageDoc extends PageResponseDoc {
    @ArraySchema(
            schema = @Schema(
                    implementation = FlightSearchResponse.class,
                    requiredMode = Schema.RequiredMode.REQUIRED
            ),
            arraySchema = @Schema(description = "List of flight search results")
    )
    public List<FlightSearchResponse> content;

    private FlightSearchPageDoc() {}
}
