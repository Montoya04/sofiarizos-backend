package com.sofiarizos.backend.service;

import com.sofiarizos.backend.dto.InscripcionDTO;
import com.sofiarizos.backend.model.Curso;
import com.sofiarizos.backend.model.Inscripcion;
import com.sofiarizos.backend.repository.CursoRepository;
import com.sofiarizos.backend.repository.InscripcionRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InscripcionService {

    private final InscripcionRepository repository;
    private final CursoRepository cursoRepository;

    public InscripcionService(InscripcionRepository repository,
                              CursoRepository cursoRepository) {
        this.repository = repository;
        this.cursoRepository = cursoRepository;
    }

    // ================= CREAR INSCRIPCIÓN =================
    @Transactional
    public Inscripcion crearInscripcion(InscripcionDTO dto, String ip) {

        // 1️⃣ Sanitizar
        String nombre = StringEscapeUtils.escapeHtml4(dto.getNombre());
        String email = StringEscapeUtils.escapeHtml4(dto.getEmail());
        String telefono = StringEscapeUtils.escapeHtml4(dto.getTelefono());
        String comentario = dto.getComentario() != null
                ? StringEscapeUtils.escapeHtml4(dto.getComentario())
                : null;
        String cursoNombre = StringEscapeUtils.escapeHtml4(dto.getCurso());

        // 2️⃣ Validar curso
        Curso curso = cursoRepository.findByNombre(cursoNombre);
        if (curso == null) {
            throw new RuntimeException("El curso no existe");
        }

        if (curso.getCupoDisponible() <= 0) {
            throw new RuntimeException("Este curso ya no tiene cupos disponibles");
        }

        // 3️⃣ Crear inscripción
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setNombre(nombre);
        inscripcion.setEmail(email);
        inscripcion.setTelefono(telefono);
        inscripcion.setComentario(comentario);
        inscripcion.setCurso(cursoNombre);
        inscripcion.setIp(ip);

        // 4️⃣ Lógica de cupos
        if (curso.getNombre().equalsIgnoreCase("Masterclass Personalizada")) {
            curso.setCupoMaximo(1);
            curso.setCupoDisponible(0);
        } else {
            curso.setCupoDisponible(curso.getCupoDisponible() - 1);
        }

        cursoRepository.save(curso);

        return repository.save(inscripcion);
    }

    // ================= LISTAR INSCRIPCIONES =================
    public List<Inscripcion> listarInscripciones() {
        return repository.findAll();
    }
}
