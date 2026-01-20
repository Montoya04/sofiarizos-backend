package com.sofiarizos.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // ✅ CORS PRIMERO
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // ✅ DESACTIVAR CSRF (API REST)
            .csrf(csrf -> csrf.disable())

            // ✅ NO SESIONES (IMPORTANTE PARA EVITAR 500)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth

                // 🔓 LOGIN Y AUTH (NO SE TOCA)
                .requestMatchers("/api/auth/**").permitAll()

                // 🔓 CURSOS Y RESERVAS (FIX DEFINITIVO)
                .requestMatchers("/api/cursos/**").permitAll()
                .requestMatchers("/api/reservas/**").permitAll()

                // 🔓 PREFLIGHT
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 🔒 LO DEMÁS
                .anyRequest().authenticated()
            )

            // ❌ NO FORM LOGIN
            .formLogin(form -> form.disable())

            // ❌ NO BASIC
            .httpBasic(basic -> basic.disable());

        return http.build();
    }

    // ✅ CORS DEFINITIVO
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "https://sofiarizos.com",
            "https://sofiarizos-frontend.vercel.app"
        ));

        config.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }

    // 🔐 PASSWORDS (NO SE TOCA)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
