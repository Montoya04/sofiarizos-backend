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
@CrossOrigin(
    origins = "https://sofiarizos-frontend.vercel.app",
    allowedHeaders = "*",
    methods = {RequestMethod.POST, RequestMethod.OPTIONS}
)
public class AdminAuthController {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAuthController(AdminRepository adminRepository,
                               PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        String password = body.get("password");

        Admin admin = adminRepository.findByEmail(email).orElse(null);

        if (admin == null || Boolean.FALSE.equals(admin.getActivo())) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Usuario o contraseña incorrectos");
        }

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Usuario o contraseña incorrectos");
        }

        return ResponseEntity.ok(
            Map.of(
                "token", "LOGIN_OK",
                "email", admin.getEmail()
            )
        );
    }

    // 👇 ESTO ARREGLA EL PREFLIGHT
    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> corsPreflight() {
        return ResponseEntity.ok().build();
    }
}
