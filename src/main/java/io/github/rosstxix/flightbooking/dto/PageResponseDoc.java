package io.github.rosstxix.flightbooking.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

// Class and its inheritors used only for OpenAPI documentation
// Not for business logic
// Inheritors MUST override:
// content field(set generic type)
// @ArraySchema(set name, description, implementation type)
@Schema(name = "!!! NOT FOR OpenAPI RESPONSE VISIBILITY !!!")
@JsonPropertyOrder({"content", "page", "size", "totalElements", "totalPages", "first", "last"})
public abstract class PageResponseDoc {
    @ArraySchema(
            schema = @Schema(name = "!!! OVERRIDE ME (content) !!!", requiredMode = Schema.RequiredMode.REQUIRED),
            arraySchema = @Schema(description = "!!! OVERRIDE ME !!!")
    )
    public List<?> content;

    @Schema(example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    public int page;

    @Schema(example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    public int size;

    @Schema(example = "31", requiredMode = Schema.RequiredMode.REQUIRED)
    public long totalElements;

    @Schema(example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    public int totalPages;

    @Schema(example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    public boolean first;

    @Schema(example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    public boolean last;

}
