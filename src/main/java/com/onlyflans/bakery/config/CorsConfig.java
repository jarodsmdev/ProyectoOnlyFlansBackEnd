package com.onlyflans.bakery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("https://onlyflans2.netlify.app", "*") //Frontend URL
//                        .allowedMethods("GET", "POST", "PUT", "DELETE")
//                        .allowedHeaders("*");
//            }
//        };
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 1. Solo permitir tu frontend real
        config.setAllowedOriginPatterns(List.of(
                "https://onlyflans2.netlify.app", // URL de producción
                "http://localhost:*", // Para desarrollo local (cualquier puerto)
                "http://s3onlyflans.s3-website-us-east-1.amazonaws.com"
        ));

        // 2. Métodos permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 3. Headers permitidos (incluye Authorization para JWT)
        config.setAllowedHeaders(List.of("*"));

        // 4. Permitir Authorization header
        // Ahora el navegador permite que el codigo js acceda al header Authorization
        config.setExposedHeaders(List.of("Authorization"));

        // 5. Permitir enviar credenciales (cookies, tokens…)
        config.setAllowCredentials(true);

        // 6. Aplica las configuraciones a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
