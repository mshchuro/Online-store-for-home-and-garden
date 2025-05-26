package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductReportDto(
        @Schema(description = "Product id", example = "1")
        Long id,

        @Schema(description = "Product name", example = "Flower")
        String name) {
}
