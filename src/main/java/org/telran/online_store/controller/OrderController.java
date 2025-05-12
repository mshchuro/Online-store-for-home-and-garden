package org.telran.online_store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.dto.CartResponseDto;
import org.telran.online_store.dto.OrderRequestDto;
import org.telran.online_store.dto.OrderResponseDto;
import org.telran.online_store.entity.Order;
import org.telran.online_store.enums.OrderStatus;
import org.telran.online_store.handler.GlobalExceptionHandler;
import org.telran.online_store.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Orders", description = "API endpoints for orders. Authorisation is required for all end-points")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;

    private final Converter<OrderRequestDto, OrderResponseDto, Order> orderConverter;

    @Operation(
            summary = "Allows to get a list of all user's orders",
            description = "Allows to view current user's orders. Available only for Administrator"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = OrderResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.UnauthorizedErrorResponse.class))})
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<Order> orders = orderService.getAll();
        return ResponseEntity.ok(orders.stream().map(orderConverter::toDto).toList());
    }

    @Operation(
            summary = "Allows to view current user's orders history",
            description = "Allows to view current user's orders history"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = OrderResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.UnauthorizedErrorResponse.class))})
    })
    @GetMapping("/history")
    public ResponseEntity<List<OrderResponseDto>> getUserHistory() {
        List<Order> orders = orderService.getAllUserOrders();
        return ResponseEntity.ok(orders.stream().map(orderConverter::toDto).toList());
    }

    @Operation(
            summary = "Getting current order status by its id",
            description = "Allows to view the current order status"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.UnauthorizedErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotFoundErrorResponse.class))})
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderStatus> getStatus(@PathVariable Long orderId) {
        Order order = orderService.getStatus(orderId);
        return ResponseEntity.ok(order.getStatus());
    }

    @Operation(
            summary = "Creating a new order",
            description = "Allows to create a new order by current user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = OrderResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Not valid data", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.ValidationErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.UnauthorizedErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = GlobalExceptionHandler.NotFoundErrorResponse.class))})
    })
    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@Valid @RequestBody OrderRequestDto dto) {
        Order order = orderConverter.toEntity(dto);
        Order saved = orderService.create(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderConverter.toDto(saved));
    }
}
