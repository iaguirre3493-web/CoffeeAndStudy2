package com.Accesos.CoffeeAndStudy.controller;

import com.Accesos.CoffeeAndStudy.domain.StudySession;
import com.Accesos.CoffeeAndStudy.service.StudySessionService;
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
@RequestMapping("/study-sessions")
@Tag(name = "Study Sessions", description = "Gestión de sesiones de estudio")
public class StudySessionController {

    @Autowired
    private StudySessionService studySessionService;

    @Operation(summary = "Obtener todas las sesiones de estudio", description = "Devuelve la lista de sesiones, con posibilidad de filtrar por placeId, userId y productivityLevel")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de sesiones obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de filtro inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<StudySession>> getAllStudySessions(
            @RequestParam(required = false) Long placeId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer productivityLevel) {
        return ResponseEntity.ok(studySessionService.filterStudySessions(placeId, userId, productivityLevel));
    }

    @Operation(summary = "Obtener sesión de estudio por id", description = "Devuelve una sesión concreta a partir de su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sesión encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Sesión no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudySession> getStudySessionById(@PathVariable Long id) {
        return ResponseEntity.ok(studySessionService.getStudySessionById(id));
    }

    @Operation(summary = "Crear sesión de estudio", description = "Crea una nueva sesión de estudio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sesión creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<StudySession> createStudySession(@Valid @RequestBody StudySession studySession) {
        StudySession savedStudySession = studySessionService.saveStudySession(studySession);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudySession);
    }

    @Operation(summary = "Actualizar sesión de estudio", description = "Actualiza una sesión existente a partir de su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sesión actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Sesión no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<StudySession> updateStudySession(@PathVariable Long id,
                                                           @Valid @RequestBody StudySession studySession) {
        return ResponseEntity.ok(studySessionService.updateStudySession(id, studySession));
    }

    @Operation(summary = "Eliminar sesión de estudio", description = "Elimina una sesión de estudio a partir de su id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sesión eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Sesión no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudySession(@PathVariable Long id) {
        studySessionService.deleteStudySession(id);
        return ResponseEntity.noContent().build();
    }
}