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
import org.telran.online_store.dto.ProductRequestDto;
import org.telran.online_store.dto.ProductResponseDto;
import org.telran.online_store.entity.Category;
import org.telran.online_store.entity.Product;
import org.telran.online_store.service.ProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private Converter<ProductRequestDto, ProductResponseDto, Product> productConverter;

    private final Product product = Product.builder()
            .id(1L)
            .name("Peony")
            .description("Pink garden peony")
            .price(new BigDecimal("15.55"))
            .discountPrice(new BigDecimal("1.55"))
            .category(Category.builder().id(1L).build())
            .imageUrl("/peony.png")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    private final ProductResponseDto responseDto = ProductResponseDto.builder()
            .id(1L)
            .name("Peony")
            .description("Pink garden peony")
            .price(new BigDecimal("15.55"))
            .discountPrice(new BigDecimal("1.55"))
            .categoryId(1L)
            .image("/peony.png")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    @Test
    @WithMockUser
    void testGetAllProducts() throws Exception {
        Mockito.when(productService.getAll(null, null, null, null, null)).thenReturn(List.of(product));
        Mockito.when(productConverter.toDto(product)).thenReturn(responseDto);

        mockMvc.perform(get("/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Peony"));
    }

    @Test
    @WithMockUser
    void testGetProductById() throws Exception {
        Mockito.when(productService.getById(1L)).thenReturn(product);
        Mockito.when(productConverter.toDto(product)).thenReturn(responseDto);

        mockMvc.perform(get("/v1/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Peony"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testCreateProduct() throws Exception {
        ProductRequestDto requestDto = ProductRequestDto.builder()
                .name("Peony")
                .description("Pink garden peony")
                .price(new BigDecimal("15.55"))
                .discountPrice(new BigDecimal("1.55"))
                .categoryId(1L)
                .image("/peony.png")
                .build();

        Mockito.when(productConverter.toEntity(any())).thenReturn(product);
        Mockito.when(productService.create(any())).thenReturn(product);
        Mockito.when(productConverter.toDto(any())).thenReturn(responseDto);

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Peony"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testUpdateProduct() throws Exception {
        ProductRequestDto requestDto = ProductRequestDto.builder()
                .name("Peony")
                .description("Pink garden peony")
                .price(new BigDecimal("15.55"))
                .discountPrice(new BigDecimal("1.55"))
                .categoryId(1L)
                .image("/peony.png")
                .build();

        Mockito.when(productConverter.toEntity(any())).thenReturn(product);
        Mockito.when(productService.updateProduct(Mockito.eq(1L), any())).thenReturn(product);
        Mockito.when(productConverter.toDto(product)).thenReturn(responseDto);

        mockMvc.perform(put("/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Peony"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/v1/products/{id}", 1L))
                .andExpect(status().isOk());

        Mockito.verify(productService).delete(1L);
    }
}