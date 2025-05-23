package org.telran.online_store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.telran.online_store.converter.Converter;
import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.dto.CartItemResponseDto;
import org.telran.online_store.dto.CartResponseDto;
import org.telran.online_store.entity.Cart;
import org.telran.online_store.entity.CartItem;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.service.CartService;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    @MockBean
    private Converter<AddToCartRequest, CartResponseDto, Cart> cartConverter;

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllItems() throws Exception {
        Cart cart = Cart.builder()
                .id(1L)
                .user(new User())
                .items(Set.of())
                .build();

        CartResponseDto responseDto = CartResponseDto.builder()
                .cartId(1L)
                .items(List.of())
                .build();

        Mockito.when(cartService.getCart()).thenReturn(cart);
        Mockito.when(cartConverter.toDto(cart)).thenReturn(responseDto);

        mockMvc.perform(get("/v1/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateCartItem() throws Exception {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        Product product = Product.builder().id(1L).build();
        CartItem item = CartItem.builder().product(product).quantity(2).build();

        Cart cart = Cart.builder()
                .id(1L)
                .items(Set.of(item))
                .build();

        CartItemResponseDto itemDto = CartItemResponseDto.builder()
                .productId(1L)
                .quantity(2)
                .build();

        CartResponseDto responseDto = CartResponseDto.builder()
                .cartId(1L)
                .items(List.of(itemDto))
                .build();

        Mockito.when(cartService.addToCart(any())).thenReturn(cart);
        Mockito.when(cartConverter.toDto(cart)).thenReturn(responseDto);

        mockMvc.perform(post("/v1/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1))
                .andExpect(jsonPath("$.items[0].productId").value(1))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteCartItem() throws Exception {
        mockMvc.perform(delete("/v1/cart/{productId}", 1L))
                .andExpect(status().isOk());

        Mockito.verify(cartService).removeFromCart(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteCart() throws Exception {
        Cart cart = Cart.builder()
                .id(1L)
                .items(Set.of())
                .build();

        CartResponseDto responseDto = CartResponseDto.builder()
                .cartId(1L)
                .items(List.of())
                .build();

        Mockito.when(cartService.clearCart()).thenReturn(cart);
        Mockito.when(cartConverter.toDto(cart)).thenReturn(responseDto);

        mockMvc.perform(delete("/v1/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1));
    }
}