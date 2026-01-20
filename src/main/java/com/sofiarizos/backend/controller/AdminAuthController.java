package com.sofiarizos.backend.controller;

import com.sofiarizos.backend.model.Admin;
import com.sofiarizos.backend.repository.AdminRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AdminAuthController {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAuthController(AdminRepository adminRepository,
                               PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔐 LOGIN ADMIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        try {
            String email = body.get("email");
            String password = body.get("password");

            System.out.println("📩 EMAIL RECIBIDO: " + email);
            System.out.println("🔑 PASSWORD RECIBIDO: " + password);

            Admin admin = adminRepository.findByEmail(email).orElse(null);

            System.out.println("👤 ADMIN ENCONTRADO: " + admin);

            if (admin == null || Boolean.FALSE.equals(admin.getActivo())) {
                return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Usuario o contraseña incorrectos");
            }


            System.out.println("🔐 PASSWORD EN BD: " + admin.getPassword());

            if (!passwordEncoder.matches(password, admin.getPassword())) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Usuario o contraseña incorrectos");
            }

            // ✅ LOGIN OK
            return ResponseEntity.ok(
                    Map.of(
                            "token", "LOGIN_OK",
                            "email", admin.getEmail()
                    )
            );

        } catch (Exception e) {
            e.printStackTrace(); // 👈 MUY IMPORTANTE
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            Map.of(
                                    "error", "Error interno del servidor",
                                    "detalle", e.getMessage()
                            )
                    );
        }
    }
}
