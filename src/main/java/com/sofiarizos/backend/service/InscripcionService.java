package com.sofiarizos.backend.service;

import com.sofiarizos.backend.dto.InscripcionDTO;
import com.sofiarizos.backend.model.Curso;
import com.sofiarizos.backend.model.Inscripcion;
import com.sofiarizos.backend.repository.CursoRepository;
import com.sofiarizos.backend.repository.InscripcionRepository;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

@Service
public class InscripcionService {

    private final InscripcionRepository repository;
    private final CursoRepository cursoRepository;

    public InscripcionService(InscripcionRepository repository, CursoRepository cursoRepository) {
        this.repository = repository;
        this.cursoRepository = cursoRepository;
    }

    public Inscripcion crearInscripcion(InscripcionDTO dto, String ip) {

        // ==========================
        // 1) Sanitizar datos
        // ==========================
        String nombre = StringEscapeUtils.escapeHtml4(dto.getNombre());
        String email = StringEscapeUtils.escapeHtml4(dto.getEmail());
        String telefono = StringEscapeUtils.escapeHtml4(dto.getTelefono());
        String comentario = dto.getComentario() != null
                ? StringEscapeUtils.escapeHtml4(dto.getComentario())
                : null;
        String cursoNombre = StringEscapeUtils.escapeHtml4(dto.getCurso());

        // ==========================
        // 2) Bloqueo de palabras maliciosas
        // ==========================
        String[] peligrosas = {
                "<script", "SELECT ", "DROP ", "DELETE ",
                "INSERT ", "--", "' OR '", "\" OR \""
        };

        for (String p : peligrosas) {
            if (nombre.contains(p) || email.contains(p) || telefono.contains(p)
                    || (comentario != null && comentario.contains(p))) {

                throw new IllegalArgumentException("Contenido inválido detectado.");
            }
        }

        // ==========================
        // 3) Verificar curso existente
        // ==========================
        Curso curso = cursoRepository.findByNombre(cursoNombre);
        if (curso == null) {
            throw new IllegalArgumentException("El curso no existe.");
        }

        if (curso.getCupoDisponible() <= 0) {
            throw new IllegalArgumentException("Este curso ya no tiene cupos disponibles.");
        }

        // ==========================
        // 4) Construir la inscripción
        // ==========================
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setNombre(nombre);
        inscripcion.setEmail(email);
        inscripcion.setTelefono(telefono);
        inscripcion.setComentario(comentario);
        inscripcion.setCurso(cursoNombre);
        inscripcion.setIp(ip); // Guardamos la IP

        // ==========================
        // 5) Restar cupo
        // ==========================
        curso.setCupoDisponible(curso.getCupoDisponible() - 1);
        cursoRepository.save(curso);

        // ==========================
        // 6) Guardar inscripción
        // ==========================
        return repository.save(inscripcion);
    }
}
