package com.sofiarizos.backend.service;

import com.sofiarizos.backend.model.Curso;
import com.sofiarizos.backend.repository.CursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    // 🟢 INSCRIPCIÓN AL CURSO
    @Transactional
    public Curso inscribirse(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        if (curso.getCupoDisponible() <= 0) {
            throw new RuntimeException("Cupos agotados");
        }

        // 🔒 Masterclass personalizada → SOLO 1 CUPO
        if (curso.getNombre().equalsIgnoreCase("Masterclass Personalizada")) {
            curso.setCupoMaximo(1);
            curso.setCupoDisponible(0);
        } else {
            // 🟢 Otros cursos normales
            curso.setCupoDisponible(curso.getCupoDisponible() - 1);
        }

        return cursoRepository.save(curso);
    }

    // 🔄 REINICIAR CUPO DESDE ADMIN
    @Transactional
    public Curso reiniciarCupo(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        if (curso.getNombre().equalsIgnoreCase("Masterclass Personalizada")) {
            curso.setCupoMaximo(1);
            curso.setCupoDisponible(1);
        } else {
            curso.setCupoDisponible(curso.getCupoMaximo());
        }

        return cursoRepository.save(curso);
    }
}
