package com.sofiarizos.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();

        // 🔴 NECESARIO para formularios y cookies
        config.setAllowCredentials(true);

        // ✅ ORÍGENES EXPLÍCITOS (NUNCA "*")
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:5173",
                "https://sofiarizos-frontend.vercel.app"
        ));

        // ✅ Headers permitidos
        config.setAllowedHeaders(List.of("*"));

        // ✅ Métodos permitidos
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
