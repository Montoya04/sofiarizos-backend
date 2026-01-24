package com.sofiarizos.backend.service;

import com.sofiarizos.backend.model.Curso;
import com.sofiarizos.backend.repository.CursoRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public Curso inscribirse(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        if (curso.getCupoDisponible() <= 0) {
            throw new RuntimeException("Cupos agotados");
        }

        // 🔒 Se descuenta UNA SOLA VEZ
        curso.setCupoDisponible(curso.getCupoDisponible() - 1);

        return curso;
    }
}

