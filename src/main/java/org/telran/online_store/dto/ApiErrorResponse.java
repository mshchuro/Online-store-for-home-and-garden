package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@Schema(description = "Response with error description")
public class ApiErrorResponse {

    @Schema(description = "HTTP status", example = "404" )
    private int status;

    @Schema(description = "Path", example = "/v1/users/1")
    private String path;

    @Schema(description = "Error message", example = "User not found")
    private String message;

    @Schema(description = "Error occurrence time", example = "2025-05-06T15:45:00")
    private String timestamp;
}
