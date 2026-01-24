package com.sofiarizos.backend.controller;

import com.sofiarizos.backend.dto.InscripcionDTO;
import com.sofiarizos.backend.model.Inscripcion;
import com.sofiarizos.backend.service.InscripcionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/inscripciones")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://sofiarizos.com",
        "https://sofiarizos-frontend.vercel.app"
})
public class InscripcionController {

    private final InscripcionService service;

    public InscripcionController(InscripcionService service) {
        this.service = service;
    }

    // ================= CREAR INSCRIPCIÓN =================
    @PostMapping
    public ResponseEntity<?> crearInscripcion(
            @Valid @RequestBody InscripcionDTO dto,
            HttpServletRequest request
    ) {
        try {
            // 🌐 IP DEL USUARIO
            String ip = request.getRemoteAddr();

            // ✅ Crear inscripción (incluye validaciones)
            Inscripcion nueva = service.crearInscripcion(dto, ip);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Inscripción realizada correctamente",
                            "data", nueva
                    )
            );

        } catch (RuntimeException e) {
            System.err.println("Error al crear inscripción: " + e.getMessage());

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", e.getMessage()
                    )
            );
        }
    }
}
