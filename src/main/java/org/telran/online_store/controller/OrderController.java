package org.telran.online_store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.dto.OrderRequestDto;
import org.telran.online_store.dto.OrderResponseDto;
import org.telran.online_store.entity.Order;
import org.telran.online_store.enums.OrderStatus;
import org.telran.online_store.service.OrderService;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController implements OrderApi{

    private final OrderService orderService;

    private final Converter<OrderRequestDto, OrderResponseDto, Order> orderConverter;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Override
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        log.info("Get all orders (ADMIN)");
        List<Order> orders = orderService.getAll();
        return ResponseEntity.ok(orders.stream().map(orderConverter::toDto).toList());
    }

    @GetMapping("/history")
    @Override
    public ResponseEntity<List<OrderResponseDto>> getUserHistory() {
        log.info("Get all order history");
        List<Order> orders = orderService.getAllUserOrders();
        return ResponseEntity.ok(orders.stream().map(orderConverter::toDto).toList());
    }

    @GetMapping("/{orderId}")
    @Override
    public ResponseEntity<OrderStatus> getStatus(@PathVariable Long orderId) {
        log.info("Get order status by id: {}", orderId);
        Order order = orderService.getStatus(orderId);
        return ResponseEntity.ok(order.getStatus());
    }

    @PostMapping
    @Override
    public ResponseEntity<OrderResponseDto> create(@Valid @RequestBody OrderRequestDto dto) {
        log.info("Creating the new order: {}", dto);
        Order order = orderConverter.toEntity(dto);
        Order saved = orderService.create(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderConverter.toDto(saved));
    }
}
