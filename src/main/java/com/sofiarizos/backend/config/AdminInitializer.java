package com.sofiarizos.backend.config;

import com.sofiarizos.backend.model.Admin;
import com.sofiarizos.backend.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initAdmin(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            String email = "admin@admin.com";

            if (adminRepository.findByEmail(email).isEmpty()) {

                Admin admin = new Admin();
                admin.setEmail(email);
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
