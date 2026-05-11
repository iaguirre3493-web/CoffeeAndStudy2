package com.Accesos.CoffeeAndStudy.controller;

import com.Accesos.CoffeeAndStudy.domain.Review;
import com.Accesos.CoffeeAndStudy.service.ReviewService;
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

@RestController
@RequestMapping("/reviews")
@Tag(name = "Reviews", description = "Gestión de reseñas")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Operation(summary = "Obtener todas las reseñas", description = "Devuelve la lista de reseñas, con posibilidad de filtrar por placeId, rating y studyFriendly")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reseñas obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de filtro inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(
            @RequestParam(required = false) Long placeId,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Boolean studyFriendly) {
        return ResponseEntity.ok(reviewService.filterReviews(placeId, rating, studyFriendly));
    }

    @Operation(summary = "Obtener reseña por id", description = "Devuelve una reseña concreta a partir de su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @Operation(summary = "Crear reseña", description = "Crea una nueva reseña")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reseña creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody Review review) {
        Review savedReview = reviewService.saveReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    @Operation(summary = "Actualizar reseña", description = "Actualiza una reseña existente a partir de su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reseña actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @Valid @RequestBody Review review) {
        return ResponseEntity.ok(reviewService.updateReview(id, review));
    }

    @Operation(summary = "Eliminar reseña", description = "Elimina una reseña a partir de su id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reseña eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}