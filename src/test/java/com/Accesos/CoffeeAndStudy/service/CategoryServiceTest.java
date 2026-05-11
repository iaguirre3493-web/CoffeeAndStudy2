package com.Accesos.CoffeeAndStudy.service;

import com.Accesos.CoffeeAndStudy.domain.Category;
import com.Accesos.CoffeeAndStudy.exception.ResourceNotFoundException;
import com.Accesos.CoffeeAndStudy.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldReturnAllCategories() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Cafe");
        category.setDescription("Cafeterias para estudiar");
        category.setColorHex("#A0522D");
        category.setPriority(1);
        category.setActive(true);
        category.setCreatedAt(LocalDate.of(2025, 10, 1));

        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<Category> result = categoryService.filterCategories(null, null, null);

        assertEquals(1, result.size());
        assertEquals("Cafe", result.get(0).getName());
    }

    @Test
    void shouldReturnCategoryById() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Cafe");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(1L);

        assertEquals("Cafe", result.getName());
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(99L));
    }

    @Test
    void shouldSaveCategory() {
        Category category = new Category();
        category.setName("Cafe");
        category.setDescription("Cafeterias para estudiar");
        category.setColorHex("#A0522D");
        category.setPriority(1);
        category.setActive(true);
        category.setCreatedAt(LocalDate.of(2025, 10, 1));

        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.saveCategory(category);

        assertEquals("Cafe", result.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void shouldUpdateCategory() {
        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setName("Cafe");

        Category updatedCategory = new Category();
        updatedCategory.setName("Library");
        updatedCategory.setDescription("Bibliotecas");
        updatedCategory.setColorHex("#123456");
        updatedCategory.setPriority(2);
        updatedCategory.setActive(true);
        updatedCategory.setCreatedAt(LocalDate.of(2025, 10, 2));

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        Category result = categoryService.updateCategory(1L, updatedCategory);

        assertEquals("Library", result.getName());
        assertEquals("Bibliotecas", result.getDescription());
    }

    @Test
    void shouldDeleteCategory() {
        Category category = new Category();
        category.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).delete(category);
    }
}