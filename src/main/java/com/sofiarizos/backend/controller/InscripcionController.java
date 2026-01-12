package com.sofiarizos.backend.controller;

import com.sofiarizos.backend.dto.InscripcionDTO;
import com.sofiarizos.backend.model.Inscripcion;
import com.sofiarizos.backend.service.InscripcionService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/inscripciones")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://sofiarizos.com"
})
public class InscripcionController {

    private final InscripcionService service;

    public InscripcionController(InscripcionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> crearInscripcion(
            @Valid @RequestBody InscripcionDTO dto,
            HttpServletRequest request   // <-- AQUI recibimos el request
    ) {
        try {

            // Obtener la IP del usuario
            String ip = request.getRemoteAddr();

            // Enviar la IP al servicio *junto con los datos*
            Inscripcion nueva = service.crearInscripcion(dto, ip);

            return ResponseEntity.ok(nueva);

        } catch (IllegalArgumentException e) {
            System.err.println("Error al crear inscripción: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Datos inválidos o incompletos"));

        }
    }

    // ============= CLASE DE RESPUESTA =============

    public static class ApiResponse {
        private boolean success;
        private String message;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
