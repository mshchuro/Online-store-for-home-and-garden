package org.telran.online_store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.dto.CategoryRequestDto;
import org.telran.online_store.dto.CategoryResponseDto;
import org.telran.online_store.entity.Category;
import org.telran.online_store.handler.GlobalExceptionHandler;
import java.util.List;

@Tag(name = "Category", description = "API endpoints for product categories")
@SecurityRequirement(name = "bearerAuth")
public interface CategoryApi {

    @Operation(
            summary = "Allows to get a list of existing categories",
            description = "Allows to view a list of categories' information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = Category.class))})
    })
    ResponseEntity<List<CategoryResponseDto>> getAll();

    @Operation(
            summary = "Allows to get a category information by it's id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = Category.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotFoundErrorResponse.class))})
    })
    ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long categoryId);

    @Operation(
            summary = "New category creating",
            description = "Allows to create a new category.\n Available only for Administrator"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = Category.class))}),
            @ApiResponse(responseCode = "400", description = "Not valid data", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.UnauthorizedErrorResponse.class))})
    })
    ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CategoryRequestDto categoryRequestDto);

    @Operation(
            summary = "Category deleting",
            description = "Allows to delete a category by it's id.\n Available only for Administrator"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Not valid data", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.UnauthorizedErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Product category is not found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotFoundErrorResponse.class))})
    })
    ResponseEntity<Void> deleteById(@PathVariable Long categoryId);

    @Operation(
            summary = "Updating category's name",
            description = "Allows to update a category's name.\n Available only for Administrator"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = Category.class))}),
            @ApiResponse(responseCode = "400", description = "Not valid data", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = "409", description = "Already exists", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotUniqueErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.UnauthorizedErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Product category is not found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotFoundErrorResponse.class))})
    })
    ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequestDto categoryRequestDto);
}
