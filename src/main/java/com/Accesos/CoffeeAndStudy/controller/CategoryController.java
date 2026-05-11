package com.Accesos.CoffeeAndStudy.controller;

import com.Accesos.CoffeeAndStudy.domain.Category;
import com.Accesos.CoffeeAndStudy.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Gestión de categorías de lugares de estudio")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Obtener todas las categorías", description = "Devuelve la lista de categorías, con posibilidad de filtrar por nombre, activo y prioridad")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de filtro inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Integer priority) {
        return ResponseEntity.ok(categoryService.filterCategories(name, active, priority));
    }

    @Operation(summary = "Obtener categoría por id - versión 2", description = "Devuelve una categoría con información adicional de versión")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/v2/{id}")
    public ResponseEntity<Map<String, Object>> getCategoryByIdV2(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);

        Map<String, Object> response = Map.of(
                "apiVersion", "v2",
                "generatedAt", LocalDate.now(),
                "data", category
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener categoría por id", description = "Devuelve una categoría concreta a partir de su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(summary = "Crear categoría", description = "Crea una nueva categoría")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        Category savedCategory = categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @Operation(summary = "Crear categoría - versión 2", description = "Crea una categoría y devuelve metadatos de versión")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/v2")
    public ResponseEntity<Map<String, Object>> createCategoryV2(@Valid @RequestBody Category category) {
        Category createdCategory = categoryService.saveCategory(category);

        Map<String, Object> response = Map.of(
                "apiVersion", "v2",
                "message", "Category created successfully",
                "data", createdCategory
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar categoría", description = "Actualiza una categoría existente a partir de su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.updateCategory(id, category));
    }

    @Operation(summary = "Actualizar categoría - versión 2", description = "Actualiza una categoría y devuelve metadatos de versión")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/v2/{id}")
    public ResponseEntity<Map<String, Object>> updateCategoryV2(@PathVariable Long id, @Valid @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);

        Map<String, Object> response = Map.of(
                "apiVersion", "v2",
                "message", "Category updated successfully",
                "data", updatedCategory
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría a partir de su id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Eliminar categoría - versión 2", description = "Elimina una categoría y devuelve confirmación con metadatos de versión")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/v2/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategoryV2(@PathVariable Long id) {
        categoryService.deleteCategory(id);

        Map<String, Object> response = Map.of(
                "apiVersion", "v2",
                "message", "Category deleted successfully",
                "deletedId", id
        );

        return ResponseEntity.ok(response);
    }
}