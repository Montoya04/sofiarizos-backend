package com.sofiarizos.backend.repository;

import com.sofiarizos.backend.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    boolean existsByEmailAndCurso(String email, String curso);
}
