package org.telran.online_store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.telran.online_store.dto.*;
import org.telran.online_store.entity.Order;
import org.telran.online_store.enums.OrderStatus;
import org.telran.online_store.service.OrderService;
import org.telran.online_store.security.JwtService;
import org.telran.online_store.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private Converter<OrderRequestDto, OrderResponseDto, Order> orderConverter;


    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetAllOrders() throws Exception {
        Order order = Order.builder()
                .id(1L)
                .deliveryAddress("Street 12")
                .deliveryMethod("Courier")
                .contactPhone("+1234567890123")
                .status(OrderStatus.PAYMENT_PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .items(List.of())
                .build();

        OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                .id(1L)
                .deliveryAddress("Street 12")
                .deliveryMethod("Courier")
                .contactPhone("+1234567890123")
                .status(OrderStatus.PAYMENT_PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .items(List.of())
                .build();

        List<Order> orders = List.of(order);

        Mockito.when(orderService.getAll()).thenReturn(orders);
        Mockito.when(orderConverter.toDto(order)).thenReturn(orderResponseDto);

        mockMvc.perform(get("/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("PAYMENT_PENDING"));
    }
    @Test
    @WithMockUser(roles = "USER")
    void testGetUserHistory() throws Exception {
        Order order = Order.builder()
                .id(1L)
                .deliveryAddress("Street 12")
                .deliveryMethod("Courier")
                .contactPhone("+1234567890123")
                .status(OrderStatus.PAYMENT_PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .items(List.of())
                .build();

        OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                .id(1L)
                .deliveryAddress("Street 12")
                .deliveryMethod("Courier")
                .contactPhone("+1234567890123")
                .status(OrderStatus.PAYMENT_PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .items(List.of())
                .build();

        List<Order> orders = List.of(order);

        Mockito.when(orderService.getAllUserOrders()).thenReturn(orders);
        Mockito.when(orderConverter.toDto(order)).thenReturn(orderResponseDto);

        mockMvc.perform(get("/v1/orders/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("PAYMENT_PENDING"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetStatus() throws Exception {
        Order order = Order.builder()
                .id(1L)
                .deliveryAddress("Street 12")
                .deliveryMethod("Courier")
                .contactPhone("+1234567890123")
                .status(OrderStatus.PAYMENT_PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .items(List.of())
                .build();

        Mockito.when(orderService.getStatus(1L)).thenReturn(order);

        mockMvc.perform(get("/v1/orders/{orderId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(order.getStatus().toString()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateOrder() throws Exception {
        OrderRequestDto requestDto = OrderRequestDto.builder()
                .deliveryAddress("Street 12")
                .deliveryMethod("Courier")
                .contactPhone("+1234567890123")
                .items(List.of(new OrderItemRequestDto(1L, 2)))
                .build();

        Order savedOrder = Order.builder()
                .id(1L)
                .status(OrderStatus.PAYMENT_PENDING)
                .build();

        OrderResponseDto responseDto = OrderResponseDto.builder()
                .id(1L)
                .status(OrderStatus.PAYMENT_PENDING)
                .deliveryAddress("Street 12")
                .deliveryMethod("Courier")
                .contactPhone("+1234567890123")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .items(List.of())
                .build();

        Mockito.when(orderService.create(any())).thenReturn(savedOrder);
        Mockito.when(orderConverter.toDto(savedOrder)).thenReturn(responseDto);

        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PAYMENT_PENDING"));
    }
}