package com.sofiarizos.backend.security;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class FileValidator {
    
    private final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png", "image/webp");
    private final long MAX_SIZE = 5 * 1024 * 1024; //5mb

    public void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Archivo vacío o inválido");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Formato de imagen no permitido");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("La imagen supera el tamaño máximo permitido (5MB)");
        }
    }
}
