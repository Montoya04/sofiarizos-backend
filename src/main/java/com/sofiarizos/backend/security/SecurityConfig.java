package com.sofiarizos.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // ===============================
    // 🔐 SECURITY FILTER CHAIN
    // ===============================
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // ❌ CSRF desactivado (API REST)
            .csrf(csrf -> csrf.disable())

            // 🌐 CORS habilitado
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // 🔓 ENDPOINTS (TODO PERMITIDO POR AHORA)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )

            // ❌ Login por formulario y basic desactivados
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }

    // ===============================
    // 🌍 CONFIGURACIÓN CORS
    // ===============================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // ✅ Orígenes permitidos
        config.setAllowedOriginPatterns(List.of(
             "http://localhost:5173",
            "https://sofiarizos-frontend.vercel.app",
            "https://sofirizos.com"
        ));


        // ✅ Métodos permitidos
        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        // ✅ Headers permitidos
        config.setAllowedHeaders(List.of("*"));

        // 🔴 IMPORTANTE para cookies / auth
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }

    // ===============================
    // 🔐 PASSWORD ENCODER
    // ===============================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
