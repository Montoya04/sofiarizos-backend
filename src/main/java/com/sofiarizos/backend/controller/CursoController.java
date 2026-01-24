package com.sofiarizos.backend.controller;

import com.sofiarizos.backend.model.Curso;
import com.sofiarizos.backend.model.Inscripcion;
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

    // ================= OBTENER TODOS LOS CURSOS =================
    @GetMapping
    public ResponseEntity<List<Curso>> obtenerCursos() {
        return ResponseEntity.ok(cursoRepository.findAll());
    }

    // ================= OBTENER CURSO POR ID =================
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCursoPorId(@PathVariable Long id) {
        Optional<Curso> curso = cursoRepository.findById(id);

        return curso.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(Map.of("message", "Curso no encontrado")));
    }

    // ================= REINICIAR CUPO (SOLO DISPONIBLE) =================
    @PutMapping("/{id}/reiniciar")
    public ResponseEntity<?> reiniciarCupo(@PathVariable Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        curso.setCupoDisponible(curso.getCupoMaximo());
        cursoRepository.save(curso);

        return ResponseEntity.ok(curso);
    }

    // ================= INSCRIPCIÓN + EMAIL =================
    // ================= INSCRIPCIÓN + EMAIL =================
    @PostMapping("/{id}/inscribirse")
        public ResponseEntity<?> inscribirseCurso(
            @PathVariable Long id,
            @RequestBody Inscripcion inscripcion
        ) {
        try {
            cursoService.inscribirse(id, inscripcion);

            new Thread(() -> {
                try {
                    emailService.notificarCurso(
                            inscripcion.getCurso(),
                            inscripcion.getNombre()
                    );
                } catch (Exception ignored) {}
            }).start();

            return ResponseEntity.ok(
                    Map.of("message", "Inscripción registrada correctamente")
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
