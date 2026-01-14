package com.sofiarizos.backend.config;

import com.sofiarizos.backend.model.Curso;
import com.sofiarizos.backend.repository.CursoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initCursos(CursoRepository cursoRepository) {
        return args -> {

            // Si ya existe, NO vuelve a crear
            if (cursoRepository.count() == 0) {
                Curso curso = new Curso();
                curso.setNombre("Masterclass personalizada");
                curso.setCupoMaximo(1);
                curso.setCupoDisponible(1);

                cursoRepository.save(curso);

                System.out.println("✅ Curso creado automáticamente");
            } else {
                System.out.println("ℹ️ Cursos ya existentes, no se crean nuevos");
            }
        };
    }
}
