package org.telran.online_store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;

    private final Converter<OrderRequestDto, OrderResponseDto, Order> orderConverter;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAll() {
        List<Order> orders = orderService.getAllUserOrders();
        return ResponseEntity.ok(orders.stream().map(orderConverter::toDto).toList());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getStatus(@PathVariable Long orderId) {
        Order order = orderService.getStatus(orderId);
        return ResponseEntity.ok(orderConverter.toDto(order));
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@RequestBody OrderRequestDto dto, Authentication authentication) {
        User user = userService.getByEmail(authentication.getName());
        Order order = orderConverter.toEntity(dto);
        order.setUser(user);
        Order saved = orderService.create(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderConverter.toDto(saved));
    }
}
