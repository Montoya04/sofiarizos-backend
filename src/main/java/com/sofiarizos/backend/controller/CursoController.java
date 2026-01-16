package com.sofiarizos.backend.controller;

import com.cloudinary.http44.api.Response;
import com.sofiarizos.backend.model.Curso;
import com.sofiarizos.backend.repository.CursoRepository;
import com.sofiarizos.backend.service.CursoService;
import com.sofiarizos.backend.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://sofiarizos.com",
        "https://sofiarizos-frontend.vercel.app"
})
public class CursoController {

    private final CursoRepository cursoRepository;
    private final CursoService cursoService;
    private final EmailService emailService;

    public CursoController(
            CursoRepository cursoRepository,
            CursoService cursoService,
            EmailService emailService
    ) {
        this.cursoRepository = cursoRepository;
        this.cursoService = cursoService;
        this.emailService = emailService;
    }

    // ================= Ajustar Cupo =========================

    @PutMapping("/{id}/ajustar-cupo")
    public ResponseEntity<?> ajustarCupo(
            @PathVariable Long id,
            @RequestParam int cupoMaximo
    ) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        curso.setCupoMaximo(cupoMaximo);
        curso.setCupoDisponible(cupoMaximo);

        cursoRepository.save(curso);

        return ResponseEntity.ok(curso);
    }

    // ================= OBTENER TODOS LOS CURSOS =================
    @GetMapping
    public ResponseEntity<List<Curso>> obtenerCursos() {
        return ResponseEntity.ok(cursoRepository.findAll());
    }

    // ================= OBTENER CURSO POR ID =================
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCursoPorId(@PathVariable Long id) {
        Optional<Curso> curso = cursoRepository.findById(id);

        if (curso.isPresent()) {
            return ResponseEntity.ok(curso.get());
        }

        return ResponseEntity.status(404)
                .body(Map.of("message", "Curso no encontrado"));
    }

    // ================= REINICIAR CUPO =================
    @PutMapping("/{id}/reiniciar")
    public ResponseEntity<?> reiniciarCupo(@PathVariable Long id) {
        Optional<Curso> cursoOpt = cursoRepository.findById(id);

        if (cursoOpt.isPresent()) {
            Curso curso = cursoOpt.get();
            curso.setCupoDisponible(curso.getCupoMaximo());
            cursoRepository.save(curso);
            return ResponseEntity.ok(curso);
        }

        return ResponseEntity.status(404)
                .body(Map.of("message", "Curso no encontrado"));
    }

    // ================= INSCRIPCIÓN + EMAIL =================
    @PostMapping("/{id}/inscribirse")
    public ResponseEntity<?> inscribirseCurso(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        try {
            String nombreAlumno = body.get("nombre");

            if (nombreAlumno == null || nombreAlumno.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "El nombre es obligatorio"));
            }

            Curso curso = cursoService.inscribirse(id);

            // 📧 Enviar email (no rompe la inscripción si falla)
            try {
                emailService.notificarCurso(
                        curso.getNombre(),
                        nombreAlumno
                );
            } catch (Exception e) {
                System.err.println("⚠️ Error enviando correo: " + e.getMessage());
            }

            return ResponseEntity.ok(
                    Map.of("message", "Inscripción realizada correctamente")
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
