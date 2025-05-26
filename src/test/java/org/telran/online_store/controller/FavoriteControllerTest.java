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
import org.telran.online_store.dto.FavoriteRequestDto;
import org.telran.online_store.dto.FavoriteResponseDto;
import org.telran.online_store.entity.Favorite;
import org.telran.online_store.entity.Product;
import org.telran.online_store.entity.User;
import org.telran.online_store.service.FavoriteService;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private Converter<FavoriteRequestDto, FavoriteResponseDto, Favorite> favoriteConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllFavorites() throws Exception {
        User user = User.builder().id(1L).build();
        Product product = Product.builder().id(10L).build();
        Favorite favorite = Favorite.builder()
                .id(1L)
                .user(user)
                .product(product)
                .build();

        FavoriteResponseDto responseDto = FavoriteResponseDto.builder()
                .id(1L)
                .productId(10L)
                .build();

        Mockito.when(favoriteService.getAll()).thenReturn(List.of(favorite));
        Mockito.when(favoriteConverter.toDto(favorite)).thenReturn(responseDto);

        mockMvc.perform(get("/v1/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].productId").value(10));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateFavorite() throws Exception {
        FavoriteRequestDto requestDto = FavoriteRequestDto.builder()
                .productId(10L)
                .build();

        User user = User.builder().id(1L).build();
        Product product = Product.builder().id(10L).build();

        Favorite favoriteEntity = Favorite.builder()
                .user(user)
                .product(product)
                .build();

        Favorite createdFavorite = Favorite.builder()
                .id(1L)
                .user(user)
                .product(product)
                .build();

        FavoriteResponseDto responseDto = FavoriteResponseDto.builder()
                .id(1L)
                .productId(10L)
                .build();

        Mockito.when(favoriteConverter.toEntity(requestDto)).thenReturn(favoriteEntity);
        Mockito.when(favoriteService.create(favoriteEntity)).thenReturn(createdFavorite);
        Mockito.when(favoriteConverter.toDto(createdFavorite)).thenReturn(responseDto);

        mockMvc.perform(post("/v1/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productId").value(10));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteFavorite() throws Exception {
        Long favoriteId = 1L;

        mockMvc.perform(delete("/v1/favorites/{favorite_id}", favoriteId))
                .andExpect(status().isOk());

        Mockito.verify(favoriteService).delete(favoriteId);
    }
}