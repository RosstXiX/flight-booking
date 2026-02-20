package io.github.rosstxix.flightbooking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Page response wrapper")
public record PageResponse<T> (

    @Schema(description = "Content of current page", requiredMode = Schema.RequiredMode.REQUIRED)
    List<T> content,

    @Schema(example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    int page,

    @Schema(example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    int size,

    @Schema(example = "31", requiredMode = Schema.RequiredMode.REQUIRED)
    long totalElements,

    @Schema(example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    int totalPages,

    @Schema(example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    boolean first,

    @Schema(example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
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
