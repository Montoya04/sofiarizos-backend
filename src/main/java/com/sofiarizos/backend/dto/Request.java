package com.sofiarizos.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.util.List;

public class Request {

    @Data
    public static class LoginDTO {
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Email inválido")
        private String email;

        @NotBlank(message = "La contraseña es obligatoria")
        private String password;
    }

    @Data
    public static class CartConfirmDTO {
        @NotBlank(message = "El nombre es obligatorio")
        private String nombre;

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Email inválido")
        private String email;

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^[0-9+\\s\\-]{7,20}$", message = "Teléfono inválido")
        private String telefono;

        private String comentario;
    }

    @Data
    public static class CursoInscripcionDTO {
        @NotBlank(message = "El nombre del alumno es obligatorio")
        private String nombre;
    }

    @Data
    public static class MasterclassInscripcionDTO {
        @NotBlank(message = "Nombre obligatorio")
        private String nombre;

        @NotBlank(message = "Email obligatorio")
        @Email(message = "Email inválido")
        private String email;

        @NotBlank(message = "Teléfono obligatorio")
        @Pattern(regexp = "^[0-9+\\s\\-]{7,20}$", message = "Teléfono inválido")
        private String telefono;
    }

}
