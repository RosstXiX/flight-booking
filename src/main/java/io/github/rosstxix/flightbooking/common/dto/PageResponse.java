package io.github.rosstxix.flightbooking.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Page of results")
public record PageResponse<T>(
        @Schema(description = "List of content on current page", requiredMode = Schema.RequiredMode.REQUIRED)
        List<T> content,
        @Schema(description = "Current page", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        int page,
        @Schema(description = "Page size", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
        int size,
        @Schema(description = "Total number of elements", example = "31", requiredMode = Schema.RequiredMode.REQUIRED)
        long totalElements,
        @Schema(description = "Total number of pages", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
        int totalPages,
        @Schema(description = "Is this page first", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        boolean first,
        @Schema(description = "Is this page last", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
        boolean last
) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

}
