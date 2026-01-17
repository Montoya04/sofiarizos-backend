package com.sofiarizos.backend;

import com.sofiarizos.backend.model.Admin;
import com.sofiarizos.backend.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    // 🔐 CREA EL ADMIN AUTOMÁTICAMENTE SI NO EXISTE
    @Bean
    CommandLineRunner initAdmin(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            if (adminRepository.count() == 0) {

                Admin admin = new Admin();
                admin.setEmail("admin@admin.com");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setActivo(true);

                adminRepository.save(admin);

                System.out.println("✅ ADMIN CREADO AUTOMÁTICAMENTE");
            } else {
                System.out.println("ℹ️ ADMIN YA EXISTE");
            }
        };
    }
}
