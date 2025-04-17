package org.telran.online_store.controller;

import jakarta.servlet.ServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.dto.OrderRequestDto;
import org.telran.online_store.dto.OrderResponseDto;
import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.User;
import org.telran.online_store.service.OrderService;
import org.telran.online_store.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;

    private final Converter<OrderRequestDto, OrderResponseDto, Order> orderConverter;

    public OrderController(OrderService orderService,
                           Converter<OrderRequestDto, OrderResponseDto, Order> orderConverter) {
        this.orderService = orderService;
        this.orderConverter = orderConverter;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAll() {
        List<Order> orders = orderService.getAll();
        return ResponseEntity.ok(orders.stream().map(orderConverter::toDto).toList());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getById(@PathVariable Long orderId) {
        Order order = orderService.getById(orderId);
        return ResponseEntity.ok(orderConverter.toDto(order));
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@RequestBody OrderRequestDto dto) {
        Order order = orderConverter.toEntity(dto);
        Order saved = orderService.create(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderConverter.toDto(saved));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable Long orderId) {
        orderService.delete(orderId);
        return ResponseEntity.noContent().build();
    }
}
