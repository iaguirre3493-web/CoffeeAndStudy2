package com.Accesos.CoffeeAndStudy.controller;

import com.Accesos.CoffeeAndStudy.dto.PlaceRequestDto;
import com.Accesos.CoffeeAndStudy.dto.PlaceResponseDto;
import com.Accesos.CoffeeAndStudy.service.PlaceService;
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
@RequestMapping("/places")
@Tag(name = "Places", description = "Gestión de lugares de estudio")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @Operation(summary = "Obtener todos los places", description = "Devuelve la lista de lugares, con posibilidad de filtrar por ciudad, wifi y activo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de places obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de filtro inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<PlaceResponseDto>> getAllPlaces(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Boolean wifiAvailable,
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(placeService.filterPlaces(city, wifiAvailable, active));
    }

    @Operation(summary = "Obtener place por id", description = "Devuelve un lugar concreto a partir de su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Place encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Place no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponseDto> getPlaceById(@PathVariable Long id) {
        return ResponseEntity.ok(placeService.getPlaceById(id));
    }

    @Operation(summary = "Crear place", description = "Crea un nuevo lugar de estudio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Place creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<PlaceResponseDto> createPlace(@Valid @RequestBody PlaceRequestDto placeDto) {
        PlaceResponseDto savedPlace = placeService.savePlace(placeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlace);
    }

    @Operation(summary = "Actualizar place", description = "Actualiza un lugar existente a partir de su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Place actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Place no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PlaceResponseDto> updatePlace(@PathVariable Long id,
                                                        @Valid @RequestBody PlaceRequestDto placeDto) {
        return ResponseEntity.ok(placeService.updatePlace(id, placeDto));
    }

    @Operation(summary = "Eliminar place", description = "Elimina un lugar a partir de su id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Place eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Place no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }
}