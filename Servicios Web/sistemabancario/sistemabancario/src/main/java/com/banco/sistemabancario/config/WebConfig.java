package com.banco.sistemabancario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                            "http://localhost:8000",                          // Desarrollo local
                            "https://tu-app.frontend.gcp.run.app",           // Cloud Run frontend (si lo usas)
                            "https://tu-dominio.web.app",                    // Firebase (si lo usas)
                            "https://storage.googleapis.com",                 // GCS bucket (frontend actual)
                            "https://orange-stone-0df8deb10.2.azurestaticapps.net/"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}

