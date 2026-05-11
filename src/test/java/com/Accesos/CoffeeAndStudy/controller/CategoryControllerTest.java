package com.Accesos.CoffeeAndStudy.controller;

import com.Accesos.CoffeeAndStudy.domain.Category;
import com.Accesos.CoffeeAndStudy.exception.GlobalExceptionHandler;
import com.Accesos.CoffeeAndStudy.exception.ResourceNotFoundException;
import com.Accesos.CoffeeAndStudy.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(categoryController, "categoryService", categoryService);

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnAllCategories() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Cafe");
        category.setDescription("Cafeterias para estudiar");
        category.setColorHex("#A0522D");
        category.setPriority(1);
        category.setActive(true);
        category.setCreatedAt(LocalDate.of(2025, 10, 1));

        when(categoryService.filterCategories(null, null, null)).thenReturn(List.of(category));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Cafe"))
                .andExpect(jsonPath("$[0].priority").value(1));
    }

    @Test
    void shouldReturnCategoryById() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Cafe");
        category.setDescription("Cafeterias para estudiar");
        category.setColorHex("#A0522D");
        category.setPriority(1);
        category.setActive(true);
        category.setCreatedAt(LocalDate.of(2025, 10, 1));

        when(categoryService.getCategoryById(1L)).thenReturn(category);

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cafe"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturn404WhenCategoryNotFound() throws Exception {
        when(categoryService.getCategoryById(99L))
                .thenThrow(new ResourceNotFoundException("Category with id 99 not found"));

        mockMvc.perform(get("/categories/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateCategory() throws Exception {
        Category category = new Category();
        category.setName("Cafe");
        category.setDescription("Cafeterias para estudiar");
        category.setColorHex("#A0522D");
        category.setPriority(1);
        category.setActive(true);
        category.setCreatedAt(LocalDate.of(2025, 10, 1));

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Cafe");
        savedCategory.setDescription("Cafeterias para estudiar");
        savedCategory.setColorHex("#A0522D");
        savedCategory.setPriority(1);
        savedCategory.setActive(true);
        savedCategory.setCreatedAt(LocalDate.of(2025, 10, 1));

        when(categoryService.saveCategory(any(Category.class))).thenReturn(savedCategory);

        mockMvc.perform(post("/categories")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Cafe"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        Category category = new Category();
        category.setName("Library");
        category.setDescription("Bibliotecas");
        category.setColorHex("#123456");
        category.setPriority(2);
        category.setActive(true);
        category.setCreatedAt(LocalDate.of(2025, 10, 2));

        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Library");
        updatedCategory.setDescription("Bibliotecas");
        updatedCategory.setColorHex("#123456");
        updatedCategory.setPriority(2);
        updatedCategory.setActive(true);
        updatedCategory.setCreatedAt(LocalDate.of(2025, 10, 2));

        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(updatedCategory);

        mockMvc.perform(put("/categories/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Library"));
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());
    }
    @Test
    void shouldReturn400WhenCreatingInvalidCategory() throws Exception {
        Category invalidCategory = new Category();
        invalidCategory.setName("");
        invalidCategory.setDescription("Cafeterias para estudiar");
        invalidCategory.setColorHex("rojo");
        invalidCategory.setPriority(1);
        invalidCategory.setActive(true);
        invalidCategory.setCreatedAt(LocalDate.of(2025, 10, 1));

        mockMvc.perform(post("/categories")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategory)))
                .andExpect(status().isBadRequest());
    }

}
