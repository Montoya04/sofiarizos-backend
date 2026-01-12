package com.sofiarizos.backend.repository;

import com.sofiarizos.backend.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    Curso findByNombre(String nombre);
}
