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
import org.telran.online_store.converter.CategoryConverter;
import org.telran.online_store.dto.CategoryRequestDto;
import org.telran.online_store.dto.CategoryResponseDto;
import org.telran.online_store.entity.Category;
import org.telran.online_store.service.CategoryService;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryConverter categoryConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllCategories() throws Exception {
        Category category = new Category(1L, "Garden");
        CategoryResponseDto responseDto = CategoryResponseDto.builder()
                .id(1L)
                .name("Garden")
                .build();

        Mockito.when(categoryService.getAllCategories()).thenReturn(List.of(category));
        Mockito.when(categoryConverter.toDto(category)).thenReturn(responseDto);

        mockMvc.perform(get("/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Garden"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testCreateCategory() throws Exception {
        CategoryRequestDto requestDto = CategoryRequestDto.builder()
                .name("Flowers")
                .build();

        Category category = new Category(1L, "Flowers");
        CategoryResponseDto responseDto = CategoryResponseDto.builder()
                .id(1L)
                .name("Flowers")
                .build();

        Mockito.when(categoryConverter.toEntity(requestDto)).thenReturn(category);
        Mockito.when(categoryService.createCategory(category)).thenReturn(category);
        Mockito.when(categoryConverter.toDto(category)).thenReturn(responseDto);

        mockMvc.perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Flowers"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetCategoryById() throws Exception {
        Long id = 10L;
        Category category = new Category(id, "Decor");
        CategoryResponseDto responseDto = CategoryResponseDto.builder()
                .id(id)
                .name("Decor")
                .build();

        Mockito.when(categoryService.getCategoryById(id)).thenReturn(category);
        Mockito.when(categoryConverter.toDto(category)).thenReturn(responseDto);

        mockMvc.perform(get("/v1/categories/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Decor"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testUpdateCategory() throws Exception {
        Long id = 100L;
        CategoryRequestDto requestDto = CategoryRequestDto.builder()
                .name("Updated")
                .build();

        Category category = new Category(id, "Updated");
        CategoryResponseDto responseDto = CategoryResponseDto.builder()
                .id(id)
                .name("Updated")
                .build();

        Mockito.when(categoryConverter.toEntity(requestDto)).thenReturn(category);
        Mockito.when(categoryService.updateCategory(id, category)).thenReturn(category);
        Mockito.when(categoryConverter.toDto(category)).thenReturn(responseDto);

        mockMvc.perform(put("/v1/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testDeleteCategory() throws Exception {
        Long id = 999L;

        mockMvc.perform(delete("/v1/categories/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(categoryService).deleteCategory(id);
    }
}