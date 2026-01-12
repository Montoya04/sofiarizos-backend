package com.sofiarizos.backend.controller;

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
@CrossOrigin(origins = "*")
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

    // ---------------- OBTENER CURSOS ----------------
    @GetMapping
    public ResponseEntity<List<Curso>> obtenerCursos() {
        return ResponseEntity.ok(cursoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCursoPorId(@PathVariable Long id) {
        Optional<Curso> curso = cursoRepository.findById(id);
        if (curso.isPresent()) {
            return ResponseEntity.ok(curso.get());
        } else {
            return ResponseEntity.status(404)
                    .body(Map.of("message", "Curso no encontrado"));
        }
    }

    // ---------------- REINICIAR CUPO ----------------
    @PutMapping("/{id}/reiniciar")
    public ResponseEntity<?> reiniciarCupo(@PathVariable Long id) {
        Optional<Curso> curso = cursoRepository.findById(id);
        if (curso.isPresent()) {
            Curso c = curso.get();
            c.setCupoDisponible(c.getCupoMaximo());
            cursoRepository.save(c);
            return ResponseEntity.ok(c);
        } else {
            return ResponseEntity.status(404)
                    .body(Map.of("message", "Curso no encontrado"));
        }
    }

    // ---------------- INSCRIPCIÓN + EMAIL ----------------
    @PostMapping("/{id}/inscribirse")
    public ResponseEntity<?> inscribirseCurso(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        try {
            String nombreAlumno = body.get("nombre");

            Curso curso = cursoService.inscribirse(id);

            // 📧 EMAIL
            emailService.notificarCurso(
                    curso.getNombre(),
                    nombreAlumno
            );

            return ResponseEntity.ok(
                    Map.of("message", "Inscripción realizada correctamente")
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
